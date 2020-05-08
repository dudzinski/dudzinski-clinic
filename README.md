# README.md

### dudzinski-clinic
dudzinski-clinic is a software based on spring-boot, written in kotlin only for recruitment reasons.
Application provides REST api for management some entities related with medical stuff like
doctors, patients, appointments and clinics.

### how to run it?
Application is standalone and if you want to run it you need just run from command line:

<code>./gradlew bootRun</code>

or if you use docker you can just run it as container using command line:
<code>./run.sh</code>

There is option to consume rest api via [Swagger](http://localhost:8080/clinic/swagger-ui.html#/).
If you prefer other http consumers like Postman you need just to know root path: http://localhost:8080/clinic/ e.g.
if you want to add patient just invoke http post method with body:
<code>
{
	"name": "Piotr",
	"surname": "Frankowicz",
	"address" : {
		"city" : "Poznań",
		"postcode" : "61-663",
		"street" : "Słowiańska",
		"houseNo" : "16A/8"
	}
}
</code>

Then if you want to see all patients just invoke http get method on url : http://localhost:8080/clinic/patients
