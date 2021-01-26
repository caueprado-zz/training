module.exports.form_include = function(application, req, res){
	res.render("admin/form_add", {validacao : {}, item : {}});
}

module.exports.item_salvar = function(application, req, res){
	var item = req.body;
	console.log(item);
	req.assert('nome','nome é obrigatório').notEmpty();
	req.assert('documento','documento é obrigatório').notEmpty();

	var erros = req.validationErrors();

	if(erros){
		res.render("admin/form_add", {validacao : erros, item : item});
		return;
	}

	var connection = application.config.dbConnection();
	var itemModel = new application.app.models.ProductDAO(connection);
	itemModel.create(item, function(error, result){
		res.redirect('/itens');
	});	
}