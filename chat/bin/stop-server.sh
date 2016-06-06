#! /bin/sh


#jps | grep GroupBootstrap | awk '{print $1}' | xargs  kill
export PID=$(jps | grep GroupBootstrap | awk '{print $1}')
echo 'find process id: '$PID
if ps -p $PID > /dev/null
then
  echo 'try to kill pid'$PID
  kill $PID
else
  echo 'no such process,maybe which has been killed.'
fi

