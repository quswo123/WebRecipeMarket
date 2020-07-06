/* ���� */
CREATE TABLE purchase (
	purchase_code NUMBER(6) NOT NULL, /* ���Ź�ȣ */
	customer_id VARCHAR(10), /* ���̵� */
	purchase_date DATE /* ���ų�¥ */
);

COMMENT ON TABLE purchase IS '����';

COMMENT ON COLUMN purchase.purchase_code IS '���Ź�ȣ';

COMMENT ON COLUMN purchase.customer_id IS '���̵�';

COMMENT ON COLUMN purchase.purchase_date IS '���ų�¥';

ALTER TABLE purchase
	ADD
		CONSTRAINT PK_purchase
		PRIMARY KEY (
			purchase_code
		);

/* ������������� */
CREATE TABLE ingredient (
	ing_code NUMBER(6) NOT NULL, /* ����ڵ� */
	ing_name VARCHAR(50) /* ����̸� */
);

COMMENT ON TABLE ingredient IS '�������������';

COMMENT ON COLUMN ingredient.ing_code IS '����ڵ�';

COMMENT ON COLUMN ingredient.ing_name IS '����̸�';

ALTER TABLE ingredient
	ADD
		CONSTRAINT PK_ingredient
		PRIMARY KEY (
			ing_code
		);

/* ��������� */
CREATE TABLE recipe_ingredient (
	recipe_code NUMBER(6) NOT NULL, /* �������ڵ� */
	ing_code NUMBER(6) NOT NULL /* ����ڵ� */
);

COMMENT ON TABLE recipe_ingredient IS '���������';

COMMENT ON COLUMN recipe_ingredient.recipe_code IS '�������ڵ�';

COMMENT ON COLUMN recipe_ingredient.ing_code IS '����ڵ�';

ALTER TABLE recipe_ingredient
	ADD
		CONSTRAINT PK_recipe_ingredient
		PRIMARY KEY (
			recipe_code,
			ing_code
		);

/* �����Ǳ⺻���� */
CREATE TABLE recipe_info (
	recipe_code NUMBER(6) NOT NULL, /* �������ڵ� */
	recipe_name VARCHAR(40), /* �������̸� */
	recipe_summ VARCHAR(250), /* �����ǿ�� */
	recipe_price NUMBER(10), /* �����ǰ��� */
	recipe_process VARCHAR(100), /* �����ǰ������ */
	recipe_status CHAR(1), /* ������Ȱ��ȭ���� */
	rd_id VARCHAR(10) /* ���������̵� */
);

COMMENT ON TABLE recipe_info IS '�����Ǳ⺻����';

COMMENT ON COLUMN recipe_info.recipe_code IS '�������ڵ�';

COMMENT ON COLUMN recipe_info.recipe_name IS '�������̸�';

COMMENT ON COLUMN recipe_info.recipe_summ IS '�����ǿ��';

COMMENT ON COLUMN recipe_info.recipe_price IS '�����ǰ���';

COMMENT ON COLUMN recipe_info.recipe_process IS '�����ǰ������';

COMMENT ON COLUMN recipe_info.recipe_status IS '������Ȱ��ȭ����';

COMMENT ON COLUMN recipe_info.rd_id IS '���������̵�';

ALTER TABLE recipe_info
	ADD
		CONSTRAINT PK_recipe_info
		PRIMARY KEY (
			recipe_code
		);

/* ���ã�� */
CREATE TABLE favorite (
	customer_id VARCHAR(10) NOT NULL, /* �������̵� */
	recipe_code NUMBER(6) NOT NULL /* �������ڵ� */
);

COMMENT ON TABLE favorite IS '���ã��';

COMMENT ON COLUMN favorite.customer_id IS '�������̵�';

COMMENT ON COLUMN favorite.recipe_code IS '�������ڵ�';

ALTER TABLE favorite
	ADD
		CONSTRAINT PK_favorite
		PRIMARY KEY (
			customer_id,
			recipe_code
		);

/* ������ */
CREATE TABLE admin (
	admin_id VARCHAR(10) NOT NULL, /* �����ھ��̵� */
	admin_pwd VARCHAR(20) /* ��й�ȣ */
);

COMMENT ON TABLE admin IS '������';

COMMENT ON COLUMN admin.admin_id IS '�����ھ��̵�';

COMMENT ON COLUMN admin.admin_pwd IS '��й�ȣ';

ALTER TABLE admin
	ADD
		CONSTRAINT PK_admin
		PRIMARY KEY (
			admin_id
		);

/* ���� */
CREATE TABLE customer (
	customer_id VARCHAR(10) NOT NULL, /* ���̵� */
	customer_pwd VARCHAR(20), /* ��й�ȣ */
	customer_email VARCHAR2(40), /* �̸��� */
	customer_name VARCHAR2(50), /* �̸� */
	customer_phone VARCHAR2(30), /* ��ȭ��ȣ */
	buildingno VARCHAR2(25), /* �ǹ�������ȣ */
	customer_addr VARCHAR(50), /* ���ּ� */
	customer_status CHAR(1) /* Ȱ��ȭ���� */
);

COMMENT ON TABLE customer IS '����';

COMMENT ON COLUMN customer.customer_id IS '���̵�';

COMMENT ON COLUMN customer.customer_pwd IS '��й�ȣ';

COMMENT ON COLUMN customer.customer_email IS '�̸���';

COMMENT ON COLUMN customer.customer_name IS '�̸�';

COMMENT ON COLUMN customer.customer_phone IS '��ȭ��ȣ';

COMMENT ON COLUMN customer.buildingno IS '�ǹ�������ȣ';

COMMENT ON COLUMN customer.customer_addr IS '���ּ�';

COMMENT ON COLUMN customer.customer_status IS 'Ȱ��ȭ����';

ALTER TABLE customer
	ADD
		CONSTRAINT PK_customer
		PRIMARY KEY (
			customer_id
		);

/* ������ */
CREATE TABLE rd (
	rd_id VARCHAR(10) NOT NULL, /* ���������̵� */
	rd_pwd VARCHAR(20), /* ��й�ȣ */
	rd_manager_name VARCHAR2(50), /* ������̸� */
	rd_team_name VARCHAR2(50), /* �μ��̸� */
	rd_phone VARCHAR2(30), /* ��ȭ��ȣ */
	rd_status CHAR(1) /* Ȱ��ȭ���� */
);

COMMENT ON TABLE rd IS '������';

COMMENT ON COLUMN rd.rd_id IS '���������̵�';

COMMENT ON COLUMN rd.rd_pwd IS '��й�ȣ';

COMMENT ON COLUMN rd.rd_manager_name IS '������̸�';

COMMENT ON COLUMN rd.rd_team_name IS '�μ��̸�';

COMMENT ON COLUMN rd.rd_phone IS '��ȭ��ȣ';

COMMENT ON COLUMN rd.rd_status IS 'Ȱ��ȭ����';

ALTER TABLE rd
	ADD
		CONSTRAINT PK_rd
		PRIMARY KEY (
			rd_id
		);

/* ���ƿ�Ⱦ�� */
CREATE TABLE point (
	recipe_code NUMBER(6) NOT NULL, /* �������ڵ� */
	like_count NUMBER(5), /* ���ƿ䰳�� */
	dislike_count NUMBER(5) /* �Ⱦ�䰳�� */
);

COMMENT ON TABLE point IS '���ƿ�Ⱦ��';

COMMENT ON COLUMN point.recipe_code IS '�������ڵ�';

COMMENT ON COLUMN point.like_count IS '���ƿ䰳��';

COMMENT ON COLUMN point.dislike_count IS '�Ⱦ�䰳��';

ALTER TABLE point
	ADD
		CONSTRAINT PK_point
		PRIMARY KEY (
			recipe_code
		);

/* ���Ż� */
CREATE TABLE purchase_detail (
	purchase_code NUMBER(6) NOT NULL, /* ���Ź�ȣ */
	recipe_code NUMBER(6) NOT NULL, /* �������ڵ� */
	purchase_quantity NUMBER(10) /* ���� */
);

COMMENT ON TABLE purchase_detail IS '���Ż�';

COMMENT ON COLUMN purchase_detail.purchase_code IS '���Ź�ȣ';

COMMENT ON COLUMN purchase_detail.recipe_code IS '�������ڵ�';

COMMENT ON COLUMN purchase_detail.purchase_quantity IS '����';

ALTER TABLE purchase_detail
	ADD
		CONSTRAINT PK_purchase_detail
		PRIMARY KEY (
			purchase_code,
			recipe_code
		);

/* �ı� */
CREATE TABLE review (
	customer_id VARCHAR(10) NOT NULL, /* �������̵� */
	recipe_code NUMBER(6) NOT NULL, /* �������ڵ� */
	review_comment VARCHAR(40), /* �ı⳻�� */
	review_date DATE /* �ۼ����� */
);

COMMENT ON TABLE review IS '�ı�';

COMMENT ON COLUMN review.customer_id IS '�������̵�';

COMMENT ON COLUMN review.recipe_code IS '�������ڵ�';

COMMENT ON COLUMN review.review_comment IS '�ı⳻��';

COMMENT ON COLUMN review.review_date IS '�ۼ�����';

ALTER TABLE review
	ADD
		CONSTRAINT PK_review
		PRIMARY KEY (
			customer_id,
			recipe_code
		);

/* �����ȣ */
CREATE TABLE Postal (
	buildingno VARCHAR2(25) NOT NULL, /* �ǹ�������ȣ */
	zipcode VARCHAR2(5), /* �����ȣ */
	sido VARCHAR2(21), /* �õ� */
	sigungu VARCHAR2(20), /* �ñ��� */
	eupmyun VARCHAR2(20), /* ���鵿 */
	dorocode VARCHAR2(12), /* ���θ��ڵ� */
	doro VARCHAR2(80), /* ���θ� */
	jiha VARCHAR2(1), /* ���Ͽ��� */
	building1 VARCHAR2(5), /* �ǹ���ȣ���� */
	building2 VARCHAR2(5), /* �ǹ���ȣ�ι� */
	daryang VARCHAR2(40), /* �ٷ����ó�� */
	building VARCHAR2(200), /* �ñ�����ǹ��� */
	dongcode VARCHAR2(10), /* �������ڵ� */
	dong VARCHAR2(20), /* �������� */
	ri VARCHAR2(20), /* ���� */
	dongadmin VARCHAR2(40), /* �������� */
	san VARCHAR2(1), /* �꿩�� */
	zibun1 VARCHAR2(4), /* �������� */
	zibunserial VARCHAR2(2), /* ���鵿�Ϸù�ȣ */
	zibun2 VARCHAR2(4), /* �����ι� */
	zipoldcode VARCHAR2(6), /* �������ȣ */
	zipcodeserial VARCHAR2(3) /* �����ȣ�Ϸù�ȣ */
);

COMMENT ON TABLE Postal IS '�����ȣ';

COMMENT ON COLUMN Postal.buildingno IS '�ǹ�������ȣ';

COMMENT ON COLUMN Postal.zipcode IS '�����ȣ';

COMMENT ON COLUMN Postal.sido IS '�õ�';

COMMENT ON COLUMN Postal.sigungu IS '�ñ���';

COMMENT ON COLUMN Postal.eupmyun IS '���鵿';

COMMENT ON COLUMN Postal.dorocode IS '���θ��ڵ�';

COMMENT ON COLUMN Postal.doro IS '���θ�';

COMMENT ON COLUMN Postal.jiha IS '���Ͽ���';

COMMENT ON COLUMN Postal.building1 IS '�ǹ���ȣ����';

COMMENT ON COLUMN Postal.building2 IS '�ǹ���ȣ�ι�';

COMMENT ON COLUMN Postal.daryang IS '�ٷ����ó��';

COMMENT ON COLUMN Postal.building IS '�ñ�����ǹ���';

COMMENT ON COLUMN Postal.dongcode IS '�������ڵ�';

COMMENT ON COLUMN Postal.dong IS '��������';

COMMENT ON COLUMN Postal.ri IS '����';

COMMENT ON COLUMN Postal.dongadmin IS '��������';

COMMENT ON COLUMN Postal.san IS '�꿩��';

COMMENT ON COLUMN Postal.zibun1 IS '��������';

COMMENT ON COLUMN Postal.zibunserial IS '���鵿�Ϸù�ȣ';

COMMENT ON COLUMN Postal.zibun2 IS '�����ι�';

COMMENT ON COLUMN Postal.zipoldcode IS '�������ȣ';

COMMENT ON COLUMN Postal.zipcodeserial IS '�����ȣ�Ϸù�ȣ';

ALTER TABLE Postal
	ADD
		CONSTRAINT postal_buildingno_pk
		PRIMARY KEY (
			buildingno
		);

ALTER TABLE purchase
	ADD
		CONSTRAINT FK_customer_TO_purchase
		FOREIGN KEY (
			customer_id
		)
		REFERENCES customer (
			customer_id
		);

ALTER TABLE recipe_ingredient
	ADD
		CONSTRAINT recipe_code_fk
		FOREIGN KEY (
			recipe_code
		)
		REFERENCES recipe_info (
			recipe_code
		);

ALTER TABLE recipe_ingredient
	ADD
		CONSTRAINT ing_code_fk
		FOREIGN KEY (
			ing_code
		)
		REFERENCES ingredient (
			ing_code
		);

ALTER TABLE recipe_info
	ADD
		CONSTRAINT rd_id_fk
		FOREIGN KEY (
			rd_id
		)
		REFERENCES rd (
			rd_id
		);

ALTER TABLE favorite
	ADD
		CONSTRAINT favorite_recipe_code_fk
		FOREIGN KEY (
			recipe_code
		)
		REFERENCES recipe_info (
			recipe_code
		);

ALTER TABLE favorite
	ADD
		CONSTRAINT favorite_customer_id_fk
		FOREIGN KEY (
			customer_id
		)
		REFERENCES customer (
			customer_id
		);

ALTER TABLE customer
	ADD
		CONSTRAINT customer_buildingno_fk
		FOREIGN KEY (
			buildingno
		)
		REFERENCES Postal (
			buildingno
		);

ALTER TABLE point
	ADD
		CONSTRAINT point_recipe_code_fk
		FOREIGN KEY (
			recipe_code
		)
		REFERENCES recipe_info (
			recipe_code
		);

ALTER TABLE purchase_detail
	ADD
		CONSTRAINT detail_purchase_code_fk
		FOREIGN KEY (
			purchase_code
		)
		REFERENCES purchase (
			purchase_code
		);

ALTER TABLE purchase_detail
	ADD
		CONSTRAINT detail_recipe_code_fk
		FOREIGN KEY (
			recipe_code
		)
		REFERENCES recipe_info (
			recipe_code
		);

ALTER TABLE review
	ADD
		CONSTRAINT review_customer_id_fk
		FOREIGN KEY (
			customer_id
		)
		REFERENCES customer (
			customer_id
		);

ALTER TABLE review
	ADD
		CONSTRAINT review_recipe_code_fk
		FOREIGN KEY (
			recipe_code
		)
		REFERENCES recipe_info (
			recipe_code
		);