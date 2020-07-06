--이미 테이블이 정의되어 있는 경우 아래 구문을 실행하여 recipe 계정을 삭제한뒤 recipe 계정을 다시 만들어주세요 (삭제코드는 주석처리해두었습니다.)

--DROP USER recipe CASCADE;

CREATE USER recipe IDENTIFIED BY recipe;
ALTER USER recipe ACCOUNT UNLOCK;
GRANT connect, resource TO recipe;