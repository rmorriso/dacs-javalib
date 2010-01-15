function openCertDetails()
{
	
	var referer = "";
	var siteSealImage;

	if (self.sampleReferer) 
		referer = self.sampleReferer;
	else
		referer = window.location; 
	
	if (document.images) {
		siteSealImage = document.images["thawteSiteSeal"]
		siteSealImage.src="https://www.thawte.com/cgi/server/seal_generator.exe?referer=" + referer
	}

	certDetailsUrlWithReferer = "https://www.thawte.com/cgi/server/certdetails.exe?referer=" + referer;
	thewindow = window.open(certDetailsUrlWithReferer,
		"newWindow", config="height=500,width=516,toolbar=no,menubar=no," +
		"scrollbars=yes,resizable=no,location=no,directories=no,status=yes");
}

