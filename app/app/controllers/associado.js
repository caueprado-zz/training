module.exports.cadastro = function(application, req, res){
	res.render("admin/cadastro_associado", {validacao : {}, item : {}});
}

module.exports.novoAssociado = function(application, req, res) {
    var axios = require('axios');
	
	var payload = req.body;

	req.assert('name','nome é obrigatório').notEmpty();
	req.assert('document','documento é obrigatório').notEmpty();

	var erros = req.validationErrors();
	
	if(erros){
		res.render("admin/cadastro_associado", {validacao : erros, pauta : payload});
		return;
	}

	axios({
		method: 'post',
		url: 'http://127.0.0.1:8091/v1/persons',
		data: payload
	}).then(response => {
        console.log(response.data.url);
        console.log(response.data.explanation);
    })
      .catch(error => {
        console.log(error);
	});

	res.render("admin/cadastro_associado", {validacao : {}, pauta : payload})
}
