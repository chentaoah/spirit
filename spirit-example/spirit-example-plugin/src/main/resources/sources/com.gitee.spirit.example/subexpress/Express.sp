
class Express {

	x=1
	y=100
	s="test"
	d=1.1
	e=d+10

	func testSubexpress(){
		if ((x+1>0)&&(y<100)) && s=="test"{
			print "hello"
		}
	}

	func testCast(){
		s1="I am a str"
		o1=(Object)s1
		print "test success",o1
	}

	func testTree(){
		//这里有一个问题s=="test"必须转换成equals
		b=(x +1 >0 && y <100 ) && s=="test" && s instanceof Object
		print "test tree",b
		list=["one","two","three"]
		b1=((Object)list.get(1)).toString().length()+100>0
		print "tree",b1
		s1=((Object)list.get(1)).toString()
		print "tree",s1
		d1=100.0
		i1=100
		num=d1+i1
		print "tree",num
		express=(x+1>0 && y<100)
		print "{}",express
		express1=((Object)list.get(1))
		print "{}",express1
		b11=(x +1 >0 && y <100 ) && list.get(0)=="test" && s instanceof Object
		print "{}",b11

		b111=( s!="test" ) && s=="test" && !empty(list.get(0))
		print "{}",b111

		b222=list.get(1).toString()
		print "{}",b222
		if s {
			return null
		} else {
			return "this is return type test"
		}

	}




}
