
DROP TABLE t_member;
-- 회원 테이블 생성
CREATE TABLE t_member(
    id VARCHAR2(20) primary key,
    pwd VARCHAR2(10),
    name VARCHAR2(50),
    email VARCHAR2(50),
    joinDate DATE DEFAULT SYSDATE
);

--회원 정보 추가
INSERT INTO t_member
VALUES('hong', '1212', '홍길동', 'hong@gmail.com', sysdate);

INSERT INTO t_member
VALUES('lee', '1212', '이순신', 'lee@test.com', sysdate);

INSERT INTO t_member
VALUES('kim', '1212', '김유신', 'kim@jweb.com', sysdate);
COMMIT;

SELECT * FROM t_member;

-- 보드  --------------------------------------------------
DROP TABLE t_board cascade CONSTRAINTS;

create table t_board (
    articleNO number(10) primary key,
    parentNO number(10) default 0,
    title varchar2(500) not null,
    content VARCHAR2(4000),
    imageFileName VARCHAR2(100),
    writedate date default sysdate not null,
    id varchar2(10),
    constraint FK_ID foreign key(id)
    references t_member(id)
);

-- 테이블에 테스트 글을 추가합니다.
insert into t_board values(1,0,'테스트글입니다.','테스트글입니다.',null,sysdate,'hong');

insert into t_board values(2,0,'안녕하세요','상품후기입니다.',null,sysdate,'hong');

insert into t_board values(3,2,'답변입니다.','상품후기에 대한 답변입니다.',null,sysdate,'hong');

insert into t_board values(5,3,'답변입니다.','상품 나쁩니다.',null,sysdate,'lee');

insert into t_board values(4,0,'김유신입니다.','김유신 테스트글입니다.',null,sysdate,'kim');

insert into t_board values(6,2,'상품 후기입니다.','이순신씨의 상품 사용후기를 올립니다..',null,sysdate,'lee');

commit;

select * from t_board;

select level, articleNO, parentNO, LPAD(' ', $*(LEVEL-1)) || title title, content, writeDate, id
from t_board
start with parentNO = 0
connect by prior articleNO=parentNO
order siblings by articleNO desc;
