#!/bin/bash
HOST=200.245.207.128
USER=L1263
PORT=8022

## Caso queira esconder a senha
# echo P@ssw0rd | base64

#PASSWORD=`echo UEBzc3cwcmQK | base64 --decode`
#PASSWORD=Am82126756
#PASSWORD=MR36049020

# $1 se refere ao primeiro parâmetro passado na command line
#FILE="$1"

#FILES=echo find /opt/Edi7Web/008/Transmitir -maxdepth 1 -mtime -1
#SFILES=($( ls -t  /opt/Edi7Web/008/Transmitir/* | head -2))
#FILES=ls -t  /opt/Edi7Web/008/Transmitir/* | head -2
FILES=($(find /opt/Edi7Web/008/Transmitir/* -maxdepth 1 -mtime -1))
#FILES="/opt/Edi7Web/008/Transmitir/*2410*"
#FILES=$SFILES
#FILES="/home/raimundo/Downloads/serasa/Transmitir"
#echo 1 $FILES

# Inicialize uma variável em branco para manter as mensagens.
MESSAGES=""
ERRORS=""


# São para notificações de totais de arquivos.
COUNT=0
ERRORCOUNT=0

# Faça um loop pelos arquivos..
#for f in $FILES
for f in ${FILES[*]}
do
#echo $f
sftp -i /root/.ssh/id_rsa -oPort=8022 -o BatchMode=no -b - $USER@$HOST << !
   cd 008-PEFIN
   put $f
   bye
!
#sftp -v -oPort=8022 -oIdentityFile=path $USER@$HOST <<EOF
#put /$f /008-PEFIN/ 
#EOF

done
