module.exports = function(application){

	application.get('/associados/cadastro', function(req, res){
		application.app.controllers.associado.cadastro(application, req, res);
	});

	application.post('/associados', function(req, res){
		application.app.controllers.associado.novoAssociado(application, req, res);
	});
}
