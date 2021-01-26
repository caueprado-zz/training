// schedule_add
module.exports = function(application) {
	application.get('/pautas/cadastro', function(req, res){
		application.app.controllers.pauta.cadastro(application, req, res);
	});

	application.get('/pautas', function(req, res) {
		application.app.controllers.pauta.pautas(application, req, res);
	});

	application.get('/pautas/votar', function(req, res) {
        application.app.controllers.pauta.pautas_votar(application, req, res);
    });

    application.post('/pautas/resultado', function(req, res) {
        application.app.controllers.pauta.resultado(application, req, res);
    });

    application.get('/pautas/resultados', function(req, res) {
        application.app.controllers.pauta.resultados(application, req, res);
    });

	application.post('/pautas/cadastrar', function(req, res) {
		application.app.controllers.pauta.novaPauta(application, req, res);
	});
}