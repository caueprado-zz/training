module.exports.cadastro = function(application, req, res){
	res.render("admin/cadastro_pauta", { validacao : {}, pauta : {} });
}

const axios = require('axios');

async function getPersons() {
  return axios({
            url: "http://127.0.0.1:8091/v1/persons",
         })
        .then(res => res.data)
        .catch(error => {
            res.send(error.data);
        });
}

async function getPautas() {
  return axios({
                url: "http://127.0.0.1:8091/v1/schedules",
            }).then(res => res.data)
            .catch(error => {
                    res.send(error.data);
            });
}

module.exports.pautas = async function(application, req, res){
	const axios = require('axios');
    console.log(pautas)
    var pautas = await getPautas();

    res.render("pauta/pautas", {pauta : pautas});
}

module.exports.pautas_votar = async function(application, req, res){
	const axios = require('axios');
    var persons = await getPersons();
    var pautas = await getPautas();

    res.render("pauta/pautas_votar", {pauta : pautas, person: persons});
}

module.exports.novaPauta = function(application, req, res) {
	const axios = require('axios');

	var payload = req.body;
	console.log(payload);
	req.assert('description','descrição é obrigatória').notEmpty();
	req.assert('category','categoria é obrigatória').notEmpty();
	req.assert('name','nome é obrigatório').notEmpty();

	var erros = req.validationErrors();

	if(erros){
		res.render("admin/cadastro_pauta", {validacao : erros, item : payload});
		return;
	}

	axios({
    		method: 'post',
    		url: 'http://127.0.0.1:8091/v1/schedules',
    		data: payload
    	}).then(response => {
            res.render("pauta/pautas", {validacao : {}, pauta : response.data});
        })
          .catch(error => {
            console.log(error);
    	});

	res.render('home/index')
}


module.exports.resultado = function(application, req, res) {
	const axios = require('axios');
    const payload = req.body;
    return axios({
            url: 'http://127.0.0.1:8091/v1/sessions/'+payload.id+'/result'
        })
        .then(response => {
            res.send(response.data);
         })
        .catch(error => {
            res.send(error.data);
        });
}

module.exports.resultados = async function(application, req, res) {
    const axios = require('axios');
    var pautas = await getPautas();
    res.render("pauta/pautas_resultado", {pauta : pautas, resultado : { total: 0 } });
}