#!/bin/bash

if [ $# -lt 5 ]; 
then
  echo "Usage: $0 RRD_DIR GRAPH_SIZE START_TIME GRAPTH_NAME TYPE [FILES]"
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
  echo "                  c: Cluster Graphs"
  echo "                  N: Node Graphs"
  echo "                  d: Disk Graphs"
  echo "                  l: Load Graphs"
  echo "                  m: Memory Graphs"
  echo "                  n: Network Graphs"
  echo "                  p: Proc Graphs"
  echo "                  s: Spawn Graphs"
  echo "                  u: CPU Graphs"
  echo "                  h: Hadoop specific graphs"
  echo "    FILES       : list of files of file expression."
  exit
fi

height=100
width=400
rrd_dir=$1
size=$2
start=now-1$3
name=$4
type=$5
exp=$6
lower_limit=0

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


# Cluster CPU Grapth
function cluster_cpu_graph()
{
  title=$1
  vertical_label="Percent"
  extras=" --rigid --font LEGEND:7"
  height=$(($height+24))
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name CPU Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:cpu_nice="cpu_nice.rrd":sum:AVERAGE \
    DEF:num_nodes="cpu_user.rrd":num:AVERAGE \
    DEF:cpu_user="cpu_user.rrd":sum:AVERAGE \
    DEF:cpu_system="cpu_system.rrd":sum:AVERAGE \
    DEF:cpu_idle="cpu_idle.rrd":sum:AVERAGE \
    DEF:cpu_wio="cpu_wio.rrd":sum:AVERAGE \
    CDEF:ccpu_nice=cpu_nice,num_nodes,/ \
    CDEF:ccpu_user=cpu_user,num_nodes,/ \
    CDEF:ccpu_system=cpu_system,num_nodes,/ \
    CDEF:ccpu_idle=cpu_idle,num_nodes,/ \
    CDEF:ccpu_wio=cpu_wio,num_nodes,/ \
    AREA:ccpu_user#3333bb:'User CPU' \
    STACK:ccpu_nice#ffea00:'Nice CPU' \
    STACK:ccpu_system#dd0000:'System CPU' \
    STACK:ccpu_wio#ff8a60:'Wait CPU' \
    STACK:ccpu_idle#e2e2f2:'Idle CPU'
}

# Cluster CPU Grapth
function node_cpu_graph()
{
  title=$1
  vertical_label="Percent"
  extras=" --rigid --font LEGEND:7"
  height=$(($height+24))
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name CPU Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:cpu_nice="cpu_nice.rrd":sum:AVERAGE \
    DEF:cpu_user="cpu_user.rrd":sum:AVERAGE \
    DEF:cpu_system="cpu_system.rrd":sum:AVERAGE \
    DEF:cpu_idle="cpu_idle.rrd":sum:AVERAGE \
    DEF:cpu_wio="cpu_wio.rrd":sum:AVERAGE \
    CDEF:ccpu_nice=cpu_nice,/ \
    CDEF:ccpu_user=cpu_user,/ \
    CDEF:ccpu_system=cpu_system,/ \
    CDEF:ccpu_idle=cpu_idle,/ \
    CDEF:ccpu_wio=cpu_wio,/ \
    AREA:ccpu_user#3333bb:'User CPU' \
    STACK:ccpu_nice#ffea00:'Nice CPU' \
    STACK:ccpu_system#dd0000:'System CPU' \
    STACK:ccpu_wio#ff8a60:'Wait CPU' \
    STACK:ccpu_idle#e2e2f2:'Idle CPU'
}


# Cluster Memory Grapth
function cluster_memory_graph()
{
  title=$1
  vertical_label="Bytes"
  extras=" --base 1024 --font LEGEND:7"
  height=$(($height-24))
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name Memory Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:mem_total="mem_total.rrd":sum:AVERAGE \
    DEF:mem_shared="mem_shared.rrd":sum:AVERAGE \
    DEF:mem_free="mem_free.rrd":sum:AVERAGE \
    DEF:mem_cached="mem_cached.rrd":sum:AVERAGE \
    DEF:mem_buffers="mem_buffers.rrd":sum:AVERAGE \
    DEF:swap_total="swap_total.rrd":sum:AVERAGE \
    DEF:swap_free="swap_free.rrd":sum:AVERAGE \
    CDEF:bmem_total=mem_total,1024,* \
    CDEF:bmem_shared=mem_shared,1024,* \
    CDEF:bmem_free=mem_free,1024,* \
    CDEF:bmem_cached=mem_cached,1024,* \
    CDEF:bmem_buffers=mem_buffers,1024,* \
    CDEF:bmem_used=bmem_total,bmem_free,-,bmem_cached,-,bmem_shared,-,bmem_buffers,- \
    CDEF:bmem_swapped=swap_total,swap_free,-,1024,* \
    AREA:bmem_used#9900CC:'Memory Used' \
    STACK:bmem_shared#5555cc:'Memory Shared' \
    STACK:bmem_cached#99ff33:'Memory Cached' \
    STACK:bmem_buffers#33cc33:'Memory Buffered' \
    STACK:bmem_swapped#0000aa:'Memory Swapped	' \
    LINE2:bmem_total#ff0000:'Total In-Core Memory'
}

# Cluster Network Graph
function cluster_network_graph()
{
  title=$1
  vertical_label="Bytes/sec"
  extras="--font LEGEND:7"
  height=$(($height+24))
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name Network Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:bytes_in="bytes_in.rrd":sum:AVERAGE \
    DEF:bytes_out="bytes_out.rrd":sum:AVERAGE \
    LINE2:bytes_in#33cc33:"In" \
    LINE2:bytes_out#5555cc:"OUT"

}

# Cluster Load Graph
function cluster_load_graph()
{
  title=$1
  vertical_label="Loads/Procs"
  extras="--font LEGEND:7"
  height=$(($height))	
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name Load Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:load_one="load_one.rrd":sum:AVERAGE \
    DEF:cpu_nodes="cpu_num.rrd":num:AVERAGE \
    DEF:cpu_num="cpu_num.rrd":sum:AVERAGE \
    DEF:proc_run="proc_run.rrd":sum:AVERAGE \
    LINE2:load_one#BBBBBB:"1-min Load" \
    LINE2:cpu_nodes#00FF00:"Nodes" \
    LINE2:cpu_num#FF0000:"CPUs" \
    LINE2:proc_run#2030F4:"Procs"
}

# Cluster Load Graph
function node_load_graph()
{
  title=$1
  vertical_label="Loads/Procs"
  extras="--font LEGEND:7"
  height=$(($height))	
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name Load Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:load_one="load_one.rrd":sum:AVERAGE \
    DEF:cpu_num="cpu_num.rrd":sum:AVERAGE \
    DEF:proc_run="proc_run.rrd":sum:AVERAGE \
    LINE2:load_one#BBBBBB:"1-min Load" \
    LINE2:cpu_num#FF0000:"CPUs" \
    LINE2:proc_run#2030F4:"Procs"
}


# Cluster Packet Graph
function cluster_packet_graph()
{
  title=$1
  vertical_label="Packets/sec"
  extras="--font LEGEND:7"
  height=$(($height))
  echo $title
  
  png_file="$name"_"$title".png
  rrdtool graph "$png_file" \
    --title "$name Packet Report" \
    --lower-limit $lower_limit \
    --vertical-label "$vertical_label" \
    $extras \
    --height $height \
    --width $width \
    --start $start \
    DEF:pkts_in="pkts_in.rrd":sum:AVERAGE \
    DEF:pkts_out="pkts_out.rrd":sum:AVERAGE \
    LINE2:pkts_in#33cc33:"In" \
    LINE2:pkts_out#5555cc:"OUT"
}

# Generic Graphs
function graphs()
{
  arr=$(echo $1 | tr "," "\n")

  for f in $arr
  do
    if [[ -f $f && -s $f ]]
    then
      title="${f%.*}"
      echo $title
      extras="--font LEGEND:7"
      png_file="$name"_"$title".png
      rrdtool graph "$png_file" \
        --title "$title" \
        --height $height \
        --width $width \
        --start $start \
        $extras \
        DEF:x=$f:sum:AVERAGE \
        AREA:x#565656
    fi
  done
}

# Cluster Graphs
function cluster_graphs()
{
  cluster_cpu_graph "cpu_report"
  cluster_memory_graph "mem_report"
  cluster_network_graph "network_report"
  cluster_load_graph "load_report"
  cluster_packet_graph "packet_report"
}

# Cluster Graphs
function node_graphs()
{
  node_cpu_graph "cpu_report"
  cluster_memory_graph "mem_report"
  cluster_network_graph "network_report"
  node_load_graph "load_report"
  cluster_packet_graph "packet_report"
}


cd "$rrd_dir"

case "$type" in
  c)  cluster_graphs
      ;;
  t)  node_graphs
      ;;
  u)  graphs "cpu_*.rrd"
      ;;
  m)  graphs "mem_*.rrd"
      ;;
  n)  graphs "bytes_*.rrd"
      graphs "pkts_*.rrd"
      ;;
  l)  graphs "load_*.rrd"
      ;;
  d)  graphs "disk_*.rrd"
      ;;
  s)  graphs "swap_*.rrd"
      ;;
  p)  graphs "proc_*.rrd"
      ;;
  h)  if [ $# -lt 5 ]; 
      then
        echo "FILE expression is missing."
        exit
      fi
      graphs "$exp"
      ;;
  *)  echo "Invalid case"    
      ;;
esac
