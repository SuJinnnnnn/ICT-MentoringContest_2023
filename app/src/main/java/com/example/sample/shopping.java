package com.example.sample;

//테이블이라고 생각하고, 테이블에 들어갈 속성값을 넣기
//파이어베이스는 RDBMS와 다르기 때문에 테이블이라는 개념이 없음. 원래는 키값이라고 부름
public class shopping {
    String product; //쇼핑 리스트 이름
    public shopping(){} // 생성자 메서드


    //getter, setter 설정
    public String getname() {
        return product;
    }

    public void setname(String name) {
        this.product = product;
    }



    //값을 추가할때 쓰는 함수, MainActivity에서 shop함수에서 사용할 것임.
    public shopping(String product){
        this.product = product;
    }
}