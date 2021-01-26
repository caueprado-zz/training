module.exports = function(application){
	application.post('/session/abrir', function(req, res){
		application.app.controllers.sessao.abrir(application, req, res);
	});

	application.post('/session/fechar', function(req, res){
    		application.app.controllers.sessao.fechar(application, req, res);
    });

    application.post('/session/votar', function(req, res){
        application.app.controllers.sessao.votar(application, req, res);
    });

    application.post('/session/resultado', function(req, res){
        application.app.controllers.sessao.resultado(application, req, res);
    });
}
