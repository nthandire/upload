package asia.grails.dbdms

class DocumentController {

    static allowedMethods = [save: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = 10
        [documentInstanceList: Document.list(params), documentInstanceTotal: Document.count()]
    }

    def create() {
    }

    def upload() {
        def file = request.getFile('file')
        if(file.empty) {
            flash.message = "File cannot be empty"
        } else {
            def documentInstance = new Document()
            documentInstance.filename = file.originalFilename
            documentInstance.filedata = file.getBytes()
            documentInstance.save()
        }
        redirect (action:'list')
    }

    def download(long id) {
        Document documentInstance = Document.get(id)
        if ( documentInstance == null) {
            flash.message = "Document not found."
            redirect (action:'list')
        } else {
            response.setContentType("APPLICATION/OCTET-STREAM")
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${documentInstance.filename}\"")

            def outputStream = response.getOutputStream()
            outputStream << documentInstance.filedata
            outputStream.flush()
            outputStream.close()
        }
    }
}

