module.exports.index = function(application, req, res){

	// var connection = application.config.dbConnection();
	// var itensModel = new application.app.models.ProductDAO(connection);

	// itensModel.get5UTopItem(function(error, result){
		// res.render("home/index", {item : result});	
	// });
	res.render("home/index");
}