var page1d = document.createElement('div');
var page2d = document.createElement('div');
var page3d = document.createElement('div');
var curpic = 0;
var calcsnum = 0;
var pic1src = 'http://static.oradestiri.ro/wp-content/uploads/2015/07/putin1.jpg';
var pic2src = 'http://static2.businessinsider.com/image/5232093ceab8ea8a68966704/meet-the-pr-firm-that-helped-vladimir-putin-troll-the-entire-country.jpg';
function login(){
	var v1 = document.getElementById('uname').value;
	var v2 = document.getElementById('pass').value;
	if(v1 === 'admin' && v2 === 'admin') {
		page1d.style.visibility = "hidden";
		page2d.style.visibility = "visible";
	}
	else {
		alert('error- wrong user name or password');
	};
};

function changepic(x) {
	if(curpic === 0){
		x.src = pic2src;
		curpic = 1;
	}else {
		x.src = pic1src;
		curpic = 0;
	};
};

function toCalc(){
	page3d.style.visibility = "visible";
	page2d.style.visibility = "hidden";
};

function logout(){
	page1d.style.visibility = "visible";
	page2d.style.visibility = "hidden";
	alert('school spirit of you logged out successfully');
};

function buttonVal(val, name){
	var btn = document.createElement('input');
	btn.setAttribute('type', 'button');
	btn.setAttribute('value', val);
	btn.setAttribute('OnClick', name + '.answer.value +="' + val + '"');
	return btn;
}

function Calc() {
	var fname = 'calc' + calcsnum;
	calcsnum += 1;
	var calcPage = document.createElement('form');
	calcPage.setAttribute('name', fname);
	var tble = document.createElement('table');
	var tmpelem1 = document.createElement('tr');
	var tmpelem2 = document.createElement('td');
	var tmpelem3 = document.createElement('input');
	tmpelem3.setAttribute('type', 'text');
	tmpelem3.setAttribute('name', 'answer');
	tmpelem2.appendChild(tmpelem3);
	tmpelem2.appendChild(document.createElement('br'));
	tmpelem1.appendChild(tmpelem2);
	tble.appendChild(tmpelem1);
	
	tmpelem1 = document.createElement('tr');
	tmpelem2 = document.createElement('td');
	tmpelem2.appendChild(buttonVal(1 ,fname));
	tmpelem2.appendChild(buttonVal(2 ,fname));
	tmpelem2.appendChild(buttonVal(3 ,fname));
	tmpelem2.appendChild(buttonVal('+' ,fname));
	tmpelem2.appendChild(document.createElement('br'));
	tmpelem1.appendChild(tmpelem2);
	tble.appendChild(tmpelem1);
	
	tmpelem1 = document.createElement('tr');
	tmpelem2 = document.createElement('td');
	tmpelem2.appendChild(buttonVal(4 ,fname));
	tmpelem2.appendChild(buttonVal(5 ,fname));
	tmpelem2.appendChild(buttonVal(6 ,fname));
	tmpelem2.appendChild(buttonVal('-' ,fname));
	tmpelem2.appendChild(document.createElement('br'));
	tmpelem1.appendChild(tmpelem2);
	tble.appendChild(tmpelem1);
	
	tmpelem1 = document.createElement('tr');
	tmpelem2 = document.createElement('td');
	tmpelem2.appendChild(buttonVal(7 ,fname));
	tmpelem2.appendChild(buttonVal(8 ,fname));
	tmpelem2.appendChild(buttonVal(9 ,fname));
	tmpelem2.appendChild(buttonVal('*' ,fname));
	tmpelem2.appendChild(document.createElement('br'));
	tmpelem1.appendChild(tmpelem2);
	tble.appendChild(tmpelem1);
	
	tmpelem1 = document.createElement('tr');
	tmpelem2 = document.createElement('td');
	tmpelem3 = document.createElement('input');
	tmpelem3.setAttribute('type', 'button');
	tmpelem3.setAttribute('value', 'c');
	tmpelem3.setAttribute('OnClick', fname + '.answer.value = ""');
	tmpelem2.appendChild(tmpelem3);
	tmpelem2.appendChild(buttonVal(0 ,fname));
	tmpelem3 = document.createElement('input');
	tmpelem3.setAttribute('type', 'button');
	tmpelem3.setAttribute('value', '=');
	tmpelem3.setAttribute('OnClick', fname + '.answer.value = ' + 'eval('+ fname +'.answer.value)' );
	tmpelem2.appendChild(tmpelem3);
	tmpelem2.appendChild(buttonVal('/' ,fname));
	tmpelem2.appendChild(document.createElement('br'));
	tmpelem1.appendChild(tmpelem2);
	tble.appendChild(tmpelem1);
	
	calcPage.appendChild(tble);
	return calcPage;
};

function buildpage1(){
	page1d.setAttribute('id', 'page1')
	page1d.appendChild(document.createTextNode("username"));
	var usname = document.createElement('input');
	usname.setAttribute('type', 'text');
	usname.setAttribute('name', 'uname');
	usname.setAttribute('id', 'uname');
	usname.setAttribute('value', 'user name');
	page1d.appendChild(usname);
	page1d.appendChild(document.createTextNode("password"));
	var pword = document.createElement('input');
	pword.setAttribute('type', 'password');
	pword.setAttribute('name', 'pass');
	pword.setAttribute('id', 'pass');
	page1d.appendChild(pword);
	var btn = document.createElement('button');
	btn.setAttribute('onclick', 'login()')
	btn.setAttribute('name', 'loginbtn');
	btn.appendChild(document.createTextNode('log in'));
	page1d.appendChild(btn);
	return page1d;
};

function buildpage2(){
	page2d.setAttribute('id', 'page2')
	page2d.appendChild(document.createTextNode('I need no introduction. Because I already introduced myself last excersize.'));
	page2d.appendChild(document.createElement('br'));
	page2d.appendChild(document.createTextNode('In case you forgot- my name is adir zagury and my hobbies are doing this excersize.'));
	page2d.appendChild(document.createElement('br'));
	page2d.appendChild(document.createTextNode('"never trust anything on the internet"/ mahatmah ghandi'));
	page2d.appendChild(document.createElement('br'));
	var btn1 = document.createElement('button');
	btn1.setAttribute('onclick', 'logout()')
	btn1.setAttribute('name', 'logoutbtn');
	btn1.appendChild(document.createTextNode('log out'));
	page2d.appendChild(btn1)
	page2d.appendChild(document.createElement('br'));
	var img = document.createElement('img');
	img.setAttribute('src', pic1src);
	img.setAttribute('onmouseover', 'changepic(this)');
	var btn2 = document.createElement('button');
	btn2.setAttribute('onclick', 'toCalc()');
	btn2.appendChild(document.createTextNode('to calculator'));
	page2d.appendChild(btn2);
	page2d.appendChild(img);
};

function addCalc(){
	page3d.appendChild(Calc());
};

function buildpage3(){
	page3d.setAttribute('id', 'page3');
	var btn = document.createElement('button');
	btn.setAttribute('onclick', 'addCalc()');
	btn.appendChild(document.createTextNode('add calculator'));
	page3d.appendChild(btn);
};

window.onload = function(){
	buildpage1();
	buildpage2();
	buildpage3();
	document.body.appendChild(page1d);
	document.body.appendChild(page2d);
	document.body.appendChild(page3d);
};
