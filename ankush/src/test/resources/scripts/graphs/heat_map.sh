#!/bin/bash

if [ $# -lt 7 ]; 
then
  echo "Usage: $0 RRD_DIR GRAPH_SIZE START_TIME GRAPTH_NAME TYPE WARNING CRITICAL"
  echo ""
  echo "    RRD_DIR     : Directory of RRDtool database file."
  echo "    GRAPH_SIZE  : Graph size"
  echo "                  s: Small size 40x130"
  echo "                  m: Medium size 75x300"
  echo "                  l: Large size 600x800"
  echo "                  x: Extra large size 1200x1600"
  echo "                  d: Default size 100x400"
  echo "    START_TIME  : Time slot to plot graph"
  echo "                  h: Last Hour"
  echo "                  d: Last Day"
  echo "                  w: Last Week"
  echo "                  m: Last Month"
  echo "                  y: Last Year"
  echo "    GRAPH_NAME  : Graph initial to identify the grapth."
  echo "    TYPE        : Graph type."
  echo "                  u: Cluster CPU Heat Map"
  echo "                  m: Cluster Memory Heat Map"
  echo "    WARNING     : Warning Level."
  echo "    CRITICAL    : Critical Level."
  exit
fi

height=100
width=400
rrd_dir=$1
size=$2
start_time=$3
start=now-1$start_time
name=$4
type=$5
warning_level=$6
critical_level=$7
xaxis=""

lower_limit=0
upper_limit=100

case "$size" in
  s)  
      height=40
      width=130
      ;;
  m)  
      height=75
      width=300
      ;;
  l)  
      height=600
      width=800
      ;;
  x)  
      height=1200
      width=1600
      ;;
  *)  
      height=100
      width=400
      ;;
esac

case "$start_time" in
  h)  
      xaxis="MINUTE:5:MINUTE:20:MINUTE:20:0:%R"
      ;;
  d)
      xaxis="HOUR:1:HOUR:6:HOUR:12:0:%a %R"
      ;;
  w)
      xaxis="HOUR:12:DAY:1:DAY:2:86400:%a %d"
      ;;
  m)
      xaxis="DAY:1:DAY:7:DAY:7:604800:Week %W"
      ;;
  y)
      xaxis="MONTH:1:MONTH:1:MONTH:1:2592000:%b"
      ;;
  *)
      echo "Invalid start time."
      exit 1
      ;;
esac

height=$(($height+24))
extras=" --rigid --font LEGEND:7"

# Cluster CPU Grapth
function cpu_heat_map()
{
  title=$1
  vertical_label="Percent"

  rrdtool graph "$title.png" \
    --title "$title" \
    --lower-limit $lower_limit \
    --upper-limit $upper_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    -x "$xaxis" \
    -c BACK#A0D5D5 -c CANVAS#FFFFFF -c ARROW#0000FF -c FRAME#CC4400 -c AXIS#0044CC \
    DEF:cpu="cpu_system.rrd":sum:AVERAGE \
    CDEF:normal=cpu,$warning_level,LE,cpu,UNKN,IF \
    CDEF:warning=cpu,$warning_level,$critical_level,LIMIT,UN,UNKN,cpu,IF \
    CDEF:critical=cpu,$critical_level,GT,cpu,UNKN,IF \
    AREA:normal#00FF00:"<$warning_level%" \
    AREA:warning#FFFF00:"$warning_level%-$critical_level%" \
    AREA:critical#FF0000:">$critical_level%"
}

# Cluster Memory Grapth
function memory_heat_map()
{
  title=$1
  vertical_label="Percent"
  
  rrdtool graph "$title.png" \
    --title "$title" \
    --lower-limit $lower_limit \
    --upper-limit $upper_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    -x "$xaxis" \
    -c BACK#A0D5D5 -c CANVAS#FFFFFF -c ARROW#0000FF -c FRAME#CC4400 -c AXIS#0044CC \
    DEF:mem_total="mem_total.rrd":sum:AVERAGE \
    DEF:mem_free="mem_free.rrd":sum:AVERAGE \
    DEF:mem_shared="mem_shared.rrd":sum:AVERAGE \
    DEF:mem_cached="mem_cached.rrd":sum:AVERAGE \
    DEF:mem_buffers="mem_buffers.rrd":sum:AVERAGE \
    CDEF:mem_used=mem_total,mem_free,-,mem_shared,-,mem_cached,-,mem_buffers,- \
    CDEF:memory=mem_used,mem_total,/,100,* \
    CDEF:normal=memory,$warning_level,LE,memory,UNKN,IF \
    CDEF:warning=memory,$warning_level,$critical_level,LIMIT,UN,UNKN,memory,IF \
    CDEF:critical=memory,$critical_level,GT,memory,UNKN,IF \
    AREA:normal#00FF00:"<$warning_level%" \
    AREA:warning#FFFF00:"$warning_level%-$critical_level%" \
    AREA:critical#FF0000:">$critical_level%"
}

cd "$rrd_dir"

case "$type" in
  u)  cpu_heat_map "$name"_cpu_heat
      ;;
  m)  memory_heat_map "$name"_memory_heat
      ;;
  *)  echo "Invalid case"    
      ;;
esac
