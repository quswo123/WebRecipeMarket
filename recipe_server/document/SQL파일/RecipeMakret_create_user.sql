--�̹� ���̺��� ���ǵǾ� �ִ� ��� �Ʒ� ������ �����Ͽ� recipe ������ �����ѵ� recipe ������ �ٽ� ������ּ��� (�����ڵ�� �ּ�ó���صξ����ϴ�.)

--DROP USER recipe CASCADE;

CREATE USER recipe IDENTIFIED BY recipe;
ALTER USER recipe ACCOUNT UNLOCK;
GRANT connect, resource TO recipe;