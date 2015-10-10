if(typeof(app)=="undefined"){
	app={};
}
app.callbacks={};
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
app.invoke=function(o){
	if(typeof(app.sendMessage)=="undefined"){
		return;
	}
	if(typeof(o)=="number"&&o.constructor==Number){
		o={id:o};
	}else if(typeof(o)=="string"&&o.constructor==String){
		o={name:o};
	}
	if(typeof(o.callback)!="undefined"){
		o.callbacker=new Date().getTime()+"";
		app.callbacks[o.callbacker]=o;
	}
	app.sendMessage(JSON.stringify({
		id:o.id,
		name:o.name,
		data:o.data,
		callbacker:o.callbacker
	}));
};