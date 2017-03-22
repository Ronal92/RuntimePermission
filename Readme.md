[ Date : 2017. 02. 01 ]

							--------------------- Today's Topic -----------------------
									(1) 접근 권한 설정(Runtime Permission)
									(2) A application에서 B application에 있는 데이터를 사용하는 경우 - 주소 버전
							----------------------------------------------------------

안드로이드 프로젝트 명 : [RuntimePermission] 

# 1. 접근 권한 설정(Runtime Permission)

## Runtime Permissions이란? 

사용자가 접근 권한이 필요한 기능( 예를 들자면, 전화 바로 걸기)을 수행할때, 사용자로 하여금 해당 권한을 앱에 허락 할 것인지 묻고, 개발자가 아닌 사용자가 자신의 디바이스의 접근 권한을 결정하는 방식입니다. 코드에서는 런타임시에 checkSelfPermission()로 자신의 권한 승인 여부를 확인하고 requestPermission()로 사용자에게 권한 승인을 요청하게 됩니다.



##코드 

![](http://i.imgur.com/sM3uhrN.png)

권한 설정을 다루는 코드는 간단합니다. 만약 전화번호부나 음악 리스트와 데이터를 사용하는 것을 사용자에게 권한 요청하는 경우에는, 위의 노란색 상자에 있는 코드들에 필요한 내용을 추가해 주면 됩니다.

예를 들어, 기본 어플리케이션에 있는 전화번호부 목록을 사용하고 싶다면, 전화번호 목록을 읽어들이는 옵션을 추가하면 됩니다.

	checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED 

	Manifest.permission.READ_CONTACTS

	grantResults[2] == PackageManager.PERMISSION_GRANTED

여기서 grantResults[]는 사용자에게 요청한 권한 목록을 배열로 담겨있습니다. 해당 옵션이 위치한 인덱스에서 런타임 권한을 확인하면 됩니다.


----------------------------------------------------------

# 2. A application에서 B application에 있는 데이터를 사용하는 경우 - 주소 버전

##2.1 전체 구조

![](http://i.imgur.com/NhoR9a3.png)

(1) MainActivity에서 런타임권한을 먼저 체크하고 loadData()를 호출합니다.
(2) loadData()에서는 DataLoader 인스턴스를 생성하고 DataLoader 멤버변수로 load()를 사용하여 주소록에 있는 정보들을 담아옵니다. 이때 Contact 클래스가 필요한 정보를 담기 위한 ArrayList로 사용됩니다.
(3) loadData()에서 RecyclerView를 생성합니다. RecyclerAdapter를 생성하면서 (2)에서 받아온 데이터를 넘깁니다.어뎁터에서는 카드뷰를 메모리에 올려놓고 받은 데이터를 카드뷰의 위젯에 세팅시키빈다.
(4) (3)에서 생성한 RecyclerView에 RecyclerAdapter를 세팅합니다.
(5) 전체적인 뷰를 LinearLayoutManger로 화면에 출력시킵니다.

##2.2 코드

###2.2.1)

![](http://i.imgur.com/gjORHt4.png)


##### 1번

![](http://cfile5.uf.tistory.com/image/1178C5014AFDAE6A083D75)


다른 애플리케이션에 있는 데이터에 접근하기 위해서는 ContentProvider를 사용해야 합니다. ContentResolver는 컨텐트 프로바이더의 주소를 통해 해당 컨텐트 프로바이더에 접근하여 컨텐트 프로바이더의 데이터에 접근할 수 있도록 해주는 역할을 합니다. 따라서 위 1번 코드는 주소록에 접근하기 위해 시스템 자원에 접근할 수 있는 context를 사용하여Content Resolver 를 불러옵니다.

( 출처 : [http://androidhuman.com/279](http://androidhuman.com/279))



##### 2번

주소록에서 가져올 데이터 컬럼명을 정의합니다. ContactsContract.CommonDataKinds는 전화번호부 데이터를 가지고 있는 일종의 데이터 테이블입니다. 여기서 kind는 그 데이터의 종류를 의미합니다. 예를 들어 CONTACT_ID는 테이블의 한 Row가 전화번호부 아이디를 나타냅니다.


##### 3번

resolver로 전화번호부 URI에 가서 data를 query 합니다. 이 때 사용하는 메소드에 들어갈 내용은 아래입니다. 메소드의 반환값은 Cursor object입니다.

							<Query method의 arguments>
							- uri(URI) : access할 object의 URI, null이 되면 안됨.
							- projection(String[]) : 접근하고자 하는 Column 또는 attribute들
							- selection(String) : return할 record들을 결정할 수 있다.
							- selectionArgs(String[]) : selection 에 대한 binding parameter들
							- sortOrder(String) : 결과의 sort 순서를 결정할 수 있다.
	( 출처 : [http://callmansoft.tistory.com/entry/Content-Provider](http://callmansoft.tistory.com/entry/Content-Provider) )

##### 4번
 Cursor란? 안드로이드에서는 DB에서 가져온 데이터를 쉽게 처리하기 위해서 Cursor 라는 인터페이스를 제공해 줍니다. Cursor는 기본적으로 DB에서 값을 가져와서 마치 실제 Table의 한 행(Row), 한 행(Row) 을 참조하는 것 처럼 사용 할 수 있게 해 줍니다. 

							<Cursor에서 사용된 메소드>
							
							- cursor.moveToNext() : Cursor를 다음 행(Row)으로 이동 시킨다.
							- cursor.getColumnINdex() : DB 테이블의 해당 필드(컬럼) 이름을 얻어 옵니다.
							- cursor.getInt() : DB 테이블에서 해당 데이터를 Integer로 가져옵니다.
							- cursor.getString() : DB 테이블에서 해당 데이터를 String로 가져옵니다.

이렇게 가져온 데이터를 ArrayList에 Contact 타입으로 저장합니다.


###2.2.2)

![](http://i.imgur.com/v7eppNb.png)

가져온 전화번호부 목록에서 다이얼 버튼을 눌렀을 때, 전화를 걸 수 있는 기능을 추가합니다. 여기서는 [week3-4]의 묵시적 인텐트 전달을 참고하면 됩니다. 

Intent 객체에 전화를 거는 컴포넌트를 발생시킬 옵션과 Url을 담아 생성시키면 됩니다.


