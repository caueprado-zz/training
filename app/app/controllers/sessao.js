module.exports.new_schedule = function(application, req, res){
	res.render("admin/schedule_add", {validacao : {}, item : {}});
}

module.exports.abrir = function(application, req, res) {
	const axios = require('axios');

    var payload = req.body;
    var minute = 10;
    var sessionId = payload.id;
    console.log(sessionId);

    axios({
        method: 'post',
        url: 'http://127.0.0.1:8091/v1/session/'+sessionId+'/open/1',
        data: payload
    }).then(response => {
        console.log(response);
    })
      .catch(error => {
        console.log(error);
    });
}

module.exports.fechar = function(application, req, res) {
	const axios = require('axios');

    var payload = req.body;
    var id = payload.id;
    console.log(id);
    axios({
    		method: 'put',
    		url: 'http://127.0.0.1:8091/v1/session/'+id+'/close',
    		data: payload
    	}).then(response => {
    	    console.log(response);
        })
          .catch(error => {
            console.log(error);
    	});
}

module.exports.votar = function(application, req, res) {
	const axios = require('axios');

    var payload = req.body;
    axios({
        method: 'post',
        url: 'http://127.0.0.1:8091/v1/session/vote',
        data: payload
    }).then(response => {
        console.log(response.data);
    })
      .catch(error => {
        console.log(error);
    });
}

