if(typeof(app)=="undefined"){
	app={};
}
app.callbacks={};
app.callbackCount=0;
app.onMessage=function(s){
	var o=JSON.parse(s);
	if(typeof(o.callbacker)!="undefined"){
		var src=app.callbacks[o.callbacker];
		if(typeof(src)!="undefined"){
			if((o.id!=null&&o.id==src.id)||(o.name!=null&&o.name==src.name)){
				src.callback(o.result);
			}
		}
		delete app.callbacks[o.callbacker];
	}
};
app.invokeLaters={};
app.invokeLaterCount=0;
app.invokeLaterId=null;
app.invokeLaterCallBack=function(){
	app.invokeLaterId=null;
	var t=new Date().getTime();
	for(var k in app.invokeLaters){
		var v=app.invokeLaters[k];
		app.invoke(v.o);
		if(t-v.t>3000){
			delete app.invokeLaters[k];
		}
	}
};
app.invokeLater=function(o){
	if(app.invokeLaterId==null){
		app.invokeLaterId=setTimeout(app.invokeLaterCallBack,300);
	}
	for(var k in app.invokeLaters){
		var v=app.invokeLaters[k];
		if(o==v.o){
			return;
		}
	}
	app.invokeLaterCount++;
	app.invokeLaters[app.invokeLaterCount]={o:o,t:new Date().getTime()};
};
app.invoke=function(o){
	if(typeof(app.sendMessage)=="undefined"){
		if(typeof(app.alias)=="undefined"){
			app.invokeLater(o);
			return;
		}else{
			if(typeof(app.alias.sendMessage)=="undefined"){
				app.invokeLater(o);
				return;
			}
			app.sendMessage=function(s){
				app.alias.sendMessage(s);
			};
			app.alias.onMessage=function(s){
				app.onMessage(s);
			};
		}
	}
	if(typeof(o)=="number"&&o.constructor==Number){
		o={id:o};
	}else if(typeof(o)=="string"&&o.constructor==String){
		o={name:o};
	}
	if(typeof(o.callback)!="undefined"){
		app.callbackCount++;
		o.callbacker=app.callbackCount+"";
		app.callbacks[o.callbacker]=o;
	}
	app.sendMessage(JSON.stringify({
		id:o.id,
		name:o.name,
		data:o.data,
		callbacker:o.callbacker
	}));
};