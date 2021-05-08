
import java.util.HashMap
import com.sum.spirit.test.example.ClassGenericTest
import com.sum.spirit.test.example.MyTest
import com.sum.spirit.test.example.GenericType

const NUMBER = 100.0
const NAME = "chentao"

@Deprecated
class Type {
	@Deprecated
	b=true
	b1=false
	i=100
	d=100.0
	s="string"
	emptyList=[]
	emptyMap={}
	list=["first","second","third"]
	list1=["string",100]
	map=[{"key1":123,"key2":"123"},{"key1":123,"key2":"123"}]
	map1={"key1":"string","key2":100}
	bArray=boolean[10]
	cArray=char[10]
	sArray=short[10]
	iArray=int[10]
	fArray=float[10]
	dArray=double[10]
	objArray=Object[10]
	strArray=String[10]
	String s111="I am a new assign syntax!"
	//测试静态访问
	num=Main.x
	myChar='c'
	const cv = "test const voliate"
	volatile vo = false

	@Deprecated
	func testType(){

		//list << "fourth"
		//测试不同类型
		//list1 << 100.0

		//map << "key3",100
		//测试不同类型
		//map1 << "key3",100.0

		//测试数组返回
		a1=bArray[0]
		a2=cArray[0]
		a3=sArray[0]
		a4=iArray[0]
		a5=fArray[0]
		a6=dArray[0]
		a7=objArray[0]
		a8=strArray[0]
		print "test array{}{}{}{}{}{}{}{}{}",a1,a2,a3,a4,a5,a6,a7,a8

		//测试class
		Class<?> clazz
		clazz=Type.class
		clz=clazz
		print "test class {}",clz

		//测试this
		self=this
		print "test this {}",self

		//测试泛型类型
		hmap=HashMap<String,String>()
		hmap.put("key","value")

		//测试long
		long1=100L
		print "long1 is ",long1

		//测试位操作符
		mask= 1L << 8
		print "mask",mask

		//测试新的数组声明方式
		ints=int[]{123, 456, 789}
		strs=String[]{"hello", "hello", "hello"}
		print "ints{}", ints
		print "strs{}", strs[0]

		yyy=1
		iii=100+yyy+++100
		iii=yyy+100
		print iii+""

		objxxx=ClassGenericTest.class.getAnnotation(MyTest.class)
		ssss=objxxx.value()
		print ""+ssss

		xxss = testParam(null, null)
		print xxss

		service=ServiceImpl<String,Object>()
		type=service.testReturnGenericType("text")
		print type.toString()

		key = service.key
		print key

		serNum=service.test1(123)
		print "" + serNum

		serStr=service.test1("hello")
		print "" + serStr

		generic=GenericType<String,String>()
		gKey=generic.get("test")
		print gKey + ""

		intsss=[1,123,8987879]
		integer=intsss.get(0)
		numberxxx=integer.intValue()
		print ""+numberxxx

		const finalNum=1000
		print "" + finalNum

		const String strxxxx
		strxxxx="hello"
		print strxxxx

		bbxxx=getArray()[0]
		print "" + bbxxx

		objvar = {
			"name" : "chen",
			"age" : 18,
			"from" : "China",
			"brother" : ["wanhao", "chenzhe"]
		}
		str0 = objvar.toString()
		print str0
	}

	func testParam(@Deprecated String str, Object obj){
		return "yes"
	}

	String testReturnType(){
		anumx=1111
		return "I am a Str!" + anumx
	}

	func getArray(){
		return bArray
	}
	
}