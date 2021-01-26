var http = require('http');

var server = http.createServer( function(req, res){

	var categoria = req.url;

	if(categoria == '/item'){
		res.end("<html><body>Notícias de Tecnologia</body></html>");
	} 
	
}).listen(3000);