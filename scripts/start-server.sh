#!/bin/bash

NGINX_CONF="/etc/nginx/sites-available/default"

cd /home/ubuntu/sscanner/docker-compose/
# 이미지 업데이트
docker pull 211125297893.dkr.ecr.ap-northeast-2.amazonaws.com/sscanner-backend

# 현재 실행 중인 서버 확인 (블루 서버가 실행 중인지 확인)
if docker ps | grep -q "blue-server"; then
  echo "블루 서버가 실행 중입니다. 그린 서버로 배포합니다."

  # 그린 서버 배포
  docker compose -f compose.green.yml up -d green --build

  # 블루 서버가 배포되는 시간 벌어주기. 이거 안해주면 그린서버가 먼저 꺼져서 502오류 뜸
  sleep 30

  # 블루 서버 삭제
  docker compose -f compose.blue.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  NEW_SERVER="green"
else
  echo "그린 서버가 실행 중입니다. 블루 서버로 배포합니다."

  # 블루 서버 배포
  docker compose -f compose.blue.yml up -d blue --build

  # 블루 서버가 배포되는 시간 벌어주기. 이거 안해주면 그린서버가 먼저 꺼져서 502오류 뜸
  sleep 30

  # 그린 서버 삭제
  docker compose -f compose.green.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  NEW_SERVER="blue"
fi

echo "------- $NEW_SERVER 서버 배포 완료 --------"
