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

  # 그린 서버가 준비될 때까지 대기
  echo "그린 서버가 준비 중입니다. 상태를 확인합니다..."
  while ! curl -sf http://localhost:8082/health; do
      sleep 2
      echo "그린 서버 준비 중..."
  done

  # 그린 서버가 준비된 후에도 약간의 추가 대기 시간 추가
  echo "그린 서버가 준비되었습니다. 블루 서버 종료 중..."
  sleep 5  # 추가 대기 시간

  # 블루 서버 삭제
  docker compose -f compose.blue.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  NEW_SERVER="green"
else
  echo "그린 서버가 실행 중입니다. 블루 서버로 배포합니다."

  # 블루 서버 배포
  docker compose -f compose.blue.yml up -d blue --build

  # 블루 서버가 준비될 때까지 대기
  echo "블루 서버가 준비 중입니다. 상태를 확인합니다..."
  while ! curl -sf http://localhost:8081/health; do
      sleep 2
      echo "블루 서버 준비 중..."
  done

  # 블루 서버가 준비된 후에도 약간의 추가 대기 시간 추가
  echo "블루 서버가 준비되었습니다. 그린 서버 종료 중..."
  sleep 5  # 추가 대기 시간

  # 그린 서버 종료
  docker compose -f compose.green.yml down

  # 사용하지 않는 이미지 삭제
  docker image prune -af

  NEW_SERVER="blue"
fi

# Nginx 재시작(reload)으로 새로운 서버로 트래픽을 보내도록 보장
# sudo service nginx reload

echo "------- $NEW_SERVER 서버 배포 완료 --------"
