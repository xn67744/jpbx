#!/bin/sh
# ============================================================
#  JPBX (FreeSWITCH GUI) launcher - Linux/macOS counterpart of start-jpbx.bat
#
#  ./jpbx.sh start     start (warn if 8090 is busy)
#  ./jpbx.sh force     stop the old instance, then start
#  ./jpbx.sh stop      stop the instance on 8090
#  ./jpbx.sh restart   stop + start
#  ./jpbx.sh status    show status + log tail
#  ./jpbx.sh service   foreground run (systemd / container style)
#
#  Environment overrides:
#  JPBX_JAVA   JDK 21 home, e.g. /usr/lib/jvm/java-21-openjdk-amd64
#  JPBX_PORT   HTTP port (default 8090)
# ============================================================

set -u

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
APP_HOME="$SCRIPT_DIR/av-admin/target"
JAR="$APP_HOME/av-admin.jar"
LOG_DIR="$SCRIPT_DIR/logs"
LOG="$LOG_DIR/jpbx-console.log"
PID_FILE="$LOG_DIR/jpbx.pid"
PORT=${JPBX_PORT:-8090}

JVM_OPTS="-Dname=av-admin.jar -Dfile.encoding=UTF-8 -Duser.timezone=GMT+8 -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED"

find_java() {
    if [ -n "${JPBX_JAVA:-}" ] && [ -x "$JPBX_JAVA/bin/java" ]; then
        echo "$JPBX_JAVA/bin/java"
        return
    fi
    if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
        echo "$JAVA_HOME/bin/java"
        return
    fi
    for candidate in /usr/lib/jvm/java-21-openjdk*/bin/java /usr/lib/jvm/java-21*/bin/java \
                     /usr/lib/jvm/jdk-21*/bin/java /opt/java/jdk-21*/bin/java /opt/jdk*/bin/java; do
        if [ -x "$candidate" ]; then
            echo "$candidate"
            return
        fi
    done
    echo java
}

port_pid() {
    if command -v ss >/dev/null 2>&1; then
        found=$(ss -H -ltnp 2>/dev/null | grep -m 1 ":$PORT " | sed -n 's/.*pid=\([0-9][0-9]*\).*/\1/p')
        [ -n "$found" ] && { echo "$found"; return; }
    fi
    if command -v lsof >/dev/null 2>&1; then
        found=$(lsof -nP -tiTCP:"$PORT" -sTCP:LISTEN 2>/dev/null | head -n 1)
        [ -n "$found" ] && { echo "$found"; return; }
    fi
    found=$(ps -ef | grep '[j]ava' | grep 'av-admin.jar' | awk '{print $2}' | head -n 1)
    [ -n "$found" ] && echo "$found"
}

check_esl() {
    if command -v ss >/dev/null 2>&1; then
        ss -H -ltn 2>/dev/null | grep -q ':8021 ' && return
    elif command -v netstat >/dev/null 2>&1; then
        netstat -ltn 2>/dev/null | grep -q ':8021 ' && return
    fi
    echo "[WARN] ESL 8021 is not listening - FreeSWITCH is not running."
    echo "       Registration / calls / CDR will be unavailable."
}

rotate_log() {
    mkdir -p "$LOG_DIR"
    if [ -f "$LOG" ]; then
        mv -f "$LOG" "$LOG.1"
    fi
}

start() {
    if [ ! -f "$JAR" ]; then
        echo "[ERROR] $JAR not found - run 'mvn clean package -Dmaven.test.skip=true' first"
        exit 1
    fi
    current=$(port_pid)
    if [ -n "$current" ]; then
        echo "[JPBX] already running (pid=$current) on port $PORT"
        return
    fi
    JAVA_CMD=$(find_java)
    check_esl
    rotate_log
    cd "$APP_HOME" || exit 1
    nohup "$JAVA_CMD" $JVM_OPTS -jar av-admin.jar >> "$LOG" 2>&1 &
    echo $! > "$PID_FILE"
    echo "[JPBX] starting... log=$LOG"
    i=0
    while [ "$i" -lt 60 ]; do
        if [ -n "$(port_pid)" ]; then
            echo "[JPBX] OK -> http://localhost:$PORT   login: admin / admin123"
            return
        fi
        i=$((i + 1))
        sleep 2
    done
    echo "[JPBX] NOT listening after ~120s. last 40 log lines:"
    tail -n 40 "$LOG" 2>/dev/null
    exit 1
}

stop() {
    current=$(port_pid)
    if [ -z "$current" ] && [ -f "$PID_FILE" ]; then
        current=$(cat "$PID_FILE")
    fi
    if [ -z "$current" ]; then
        echo "[JPBX] nothing listening on $PORT"
        rm -f "$PID_FILE"
        return
    fi
    echo "[JPBX] stopping PID $current ..."
    kill -TERM "$current" 2>/dev/null
    i=0
    while [ "$i" -lt 30 ]; do
        if [ -z "$(port_pid)" ]; then
            echo "[JPBX] stopped"
            rm -f "$PID_FILE"
            return
        fi
        i=$((i + 1))
        sleep 1
    done
    kill -KILL "$current" 2>/dev/null
    rm -f "$PID_FILE"
    echo "[JPBX] force stopped"
}

status() {
    current=$(port_pid)
    if [ -n "$current" ]; then
        echo "[JPBX] running  PID=$current  http://localhost:$PORT"
    else
        echo "[JPBX] not running"
    fi
    if [ -f "$LOG" ]; then
        echo "-------- last 8 lines of $LOG --------"
        tail -n 8 "$LOG"
    fi
}

service() {
    if [ ! -f "$JAR" ]; then
        echo "[ERROR] $JAR not found"
        exit 1
    fi
    JAVA_CMD=$(find_java)
    check_esl
    cd "$APP_HOME" || exit 1
    exec "$JAVA_CMD" $JVM_OPTS -jar av-admin.jar
}

case "${1:-}" in
    start)   start ;;
    stop)    stop ;;
    restart) stop; sleep 2; start ;;
    force)   stop; sleep 2; start ;;
    status)  status ;;
    service) service ;;
    *)
        echo "用法: $0 {start|stop|restart|force|status|service}"
        exit 1
        ;;
esac
