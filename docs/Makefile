XSLTPROC = xsltproc
INSTALL = install

## The following variables must be passed as arguments to make:
## BOOK_DIR, BOOK_NAME, INSTALL_DIR

## You shouldn't normally need to edit anything below here.
SHELL = /bin/sh
DACSDOCSVERSION = svnversion
DOCS_ROOT = .

CSS_DIR = ${DOCS_ROOT}/css
BOOK_HTML_CHUNK_DIR = $(BOOK_DIR)/$(BOOK_NAME)-chunk
BOOK_HTML_TARGET = $(BOOK_DIR)/$(BOOK_NAME).html
BOOK_HTML_CHUNK_TARGET = $(BOOK_HTML_CHUNK_DIR)/index.html  # index.html is created last
BOOK_PDF_TARGET = $(BOOK_DIR)/$(BOOK_NAME).pdf
BOOK_PS_TARGET = $(BOOK_DIR)/$(BOOK_NAME).ps
BOOK_FO_TARGET = $(BOOK_DIR)/$(BOOK_NAME).fo
BOOK_XML_SOURCE = $(BOOK_DIR)/$(BOOK_NAME).xml
BOOK_VERSION_SOURCE =  $(DOCS_ROOT)/ent/version.ent
BOOK_ALL_SOURCE = $(BOOK_DIR)/*.xml
BOOK_IMAGES = $(BOOK_DIR)/images
BOOK_INSTALL_DIR = $(INSTALL_DIR)/docs/$(BOOK_NAME)

XSL_FO = ${DOCS_ROOT}/dacs-fo-stylesheet.xsl
XSL_HTML = $(DOCS_ROOT)/dacs-html-stylesheet.xsl
XSL_HTML_CHUNK = $(DOCS_ROOT)/dacs-chunk-stylesheet.xsl

RUN_FOP = ./bin/run-fop.sh

# Book xsltproc options for HTML output
# Note: --stringparam arguments no longer go here; 
# see xsl/dacs-html-stylesheet.xsl and xsl/dacs-chunk-stylesheet.xsl
BOOK_HTML_XSLTPROC_OPTS = 

# Book xsltproc options for PDF and PostScript output
# BOOK_PDF_XSLTPROC_OPTS = --stringparam page.height 9in --stringparam page.width 6.4in
# BOOK_PS_XSLTPROC_OPTS = --stringparam page.height 9in --stringparam page.width 6.4in

# Uncomment the following line if you'd like to print on A4 paper
# BOOK_PDF_XSLTPROC_OPTS = --stringparam paper.type A4

all: docbook-xml docbook-xsl all-html all-pdf all-ps

docbook-xml:
	@( test ! -L docbook-xml && echo -e "\n ** first create link to docbook-xml location \n" && exit 1 )

docbook-xsl:
	@( test ! -L docbook-xsl && echo -e "\n ** first create link to docbook-xsl location \n" && exit 1 )


html-docbook.xsl:
	echo create link to docbook xsl stylesheet for html manpages
	echo example: ln -s ../../docbook-xsl/html/docbook.xsl html-docbook.xsl


install: install-book 

all-html: book-html book-html-chunk 

all-pdf: book-pdf 

all-ps: book-ps 

all-book: book-html book-html-chunk book-pdf book-ps

install-book: install-book-html install-book-html-chunk install-book-pdf install-book-ps

clean: book-clean misc-docs-clean

$(BOOK_VERSION_SOURCE): book-version

book-version:
	@if $(DACSDOCSVERSION) . > /dev/null; then \
	echo '<!ENTITY dacsdocs.version "Revision '`$(DACSDOCSVERSION) .`'">' > $(BOOK_VERSION_SOURCE).tmp; \
	else \
	echo '<!ENTITY dacsdocs.version "">' > $(BOOK_VERSION_SOURCE).tmp; \
	fi
	@if cmp -s $(BOOK_VERSION_SOURCE) $(BOOK_VERSION_SOURCE).tmp; then \
	rm $(BOOK_VERSION_SOURCE).tmp; \
	else \
	mv $(BOOK_VERSION_SOURCE).tmp $(BOOK_VERSION_SOURCE); \
	fi

book-html: $(BOOK_HTML_TARGET)

$(BOOK_HTML_TARGET): $(BOOK_ALL_SOURCE) $(BOOK_VERSION_SOURCE)
	$(XSLTPROC) $(BOOK_HTML_XSLTPROC_OPTS) \
           --output $(BOOK_HTML_TARGET) $(XSL_HTML) $(BOOK_XML_SOURCE)

book-html-chunk: $(BOOK_HTML_CHUNK_TARGET)

## This trailing slash is essential that xsltproc will output pages to the dir
$(BOOK_HTML_CHUNK_TARGET): $(BOOK_ALL_SOURCE) $(BOOK_VERSION_SOURCE) \
                           $(CSS_DIR)/mldocs.css $(BOOK_IMAGES)
	mkdir -p $(BOOK_HTML_CHUNK_DIR)
	$(XSLTPROC) $(BOOK_HTML_XSLTPROC_OPTS) \
           --output $(BOOK_HTML_CHUNK_DIR)/ \
	   $(XSL_HTML_CHUNK) $(BOOK_XML_SOURCE)

book-pdf: $(BOOK_PDF_TARGET)

book-ps: $(BOOK_PS_TARGET)

$(BOOK_PDF_TARGET): $(BOOK_ALL_SOURCE) $(BOOK_VERSION_SOURCE) $(BOOK_IMAGES)
	$(XSLTPROC) $(BOOK_PDF_XSLTPROC_OPTS) \
	   --output $(BOOK_FO_TARGET) $(XSL_FO) $(BOOK_XML_SOURCE)
	$(RUN_FOP) $(DOCS_ROOT) -fo $(BOOK_FO_TARGET) -pdf $(BOOK_PDF_TARGET)

$(BOOK_PS_TARGET): $(BOOK_ALL_SOURCE) $(BOOK_VERSION_SOURCE) $(BOOK_IMAGES)
	$(XSLTPROC) $(BOOK_PS_XSLTPROC_OPTS) \
	   --output $(BOOK_FO_TARGET) $(XSL_FO) $(BOOK_XML_SOURCE)
	$(RUN_FOP) $(DOCS_ROOT) -fo $(BOOK_FO_TARGET) -ps $(BOOK_PS_TARGET)

$(BOOK_INSTALL_DIR):
	$(INSTALL) -d $(BOOK_INSTALL_DIR)

install-book-html: $(BOOK_HTML_TARGET)
	$(INSTALL) -d $(BOOK_INSTALL_DIR)/images
	$(INSTALL) $(BOOK_HTML_TARGET) $(BOOK_INSTALL_DIR)
	$(INSTALL) $(BOOK_IMAGES) $(BOOK_INSTALL_DIR)/images

install-book-html-chunk: $(BOOK_HTML_CHUNK_TARGET)
	$(INSTALL) -d $(BOOK_INSTALL_DIR)/images
	$(INSTALL) $(BOOK_HTML_CHUNK_DIR)/*.html $(BOOK_INSTALL_DIR)
	$(INSTALL) $(BOOK_IMAGES) $(BOOK_INSTALL_DIR)/images

install-book-pdf: $(BOOK_PDF_TARGET) $(BOOK_INSTALL_DIR)
	$(INSTALL) $(BOOK_PDF_TARGET) $(BOOK_INSTALL_DIR)

install-book-ps: $(BOOK_PS_TARGET) $(BOOK_INSTALL_DIR)
	$(INSTALL) $(BOOK_PS_TARGET) $(BOOK_INSTALL_DIR)

install-css:
	$(INSTALL) $(CSS_DIR)/mldocs.css $(INSTALL_DIR)/css
book-clean:
	rm -f $(BOOK_VERSION_SOURCE)
	rm -f $(BOOK_HTML_TARGET) $(BOOK_FO_TARGET)
	rm -rf $(BOOK_HTML_CHUNK_DIR)
	rm -f $(BOOK_PDF_TARGET) $(BOOK_PS_TARGET) 

