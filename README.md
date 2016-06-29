# Aula8-PDFViewer
Visor de PDF para la Pizarra de Aula 8 

Instrucciones de Uso: 

# 1.- Importar el .jar que se encuentra en este repositorio
  https://sintaxispragmatica.wordpress.com/2013/07/18/anadir-librerias-jar-en-androidstudio/
  
# 2.- Copiar el Archivo PDFImageView.java 

# Para Instanciar el Visor de PDF es necesario un ImageView en el Layout del Activity en el que se vaya a usar
  ```
  PDFImageView instancia = new PDFImageView(String DireccionArchivo, ImageView imageView);
  ```
  esto Instanciara el Visor y lo pondra en la Primera Pagina del Archivo seleccionado.
  
# Para Moverse Entre Paginas y Hacer Zoom
  ```
  instancia.nextPage(); // Pagina Siguiente
  
  instancia.prevPage()// Pagina Anterior
  
  instancia.gotoPage(int NumeroPagina) // ir a un Numero de Pagina
  
  instancia.zoomIn() // Hacer Zoom
  
  instancia.zoomOut() // retroceder el Zoom
  ```

Colaboradores: Johans Cedeño, Osner Sanchez, Rover Gonzalez




