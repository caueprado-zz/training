module.exports = function(application){
	application.get('/formulario_include', function(req, res){
		application.app.controllers.admin.form_include(application, req, res);
	});

	application.post('/itens/salvar', function(req, res){
		application.app.controllers.admin.item_salvar(application, req, res);
	});
}