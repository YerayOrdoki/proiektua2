from pathlib import Path
import json, os, textwrap, xml.etree.ElementTree as ET
from xml.sax.saxutils import escape
from docx import Document
from docx.shared import Inches, Pt
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Image, Preformatted
from reportlab.lib.styles import getSampleStyleSheet
from PIL import Image as PILImage

root = Path(__file__).resolve().parents[1]
out = root / 'doc/patterns'
reports = list((root / 'target/surefire-reports').glob('TEST-*Patterns*Test.xml'))
counts = dict(tests=0, failures=0, errors=0, skipped=0)
for path in reports:
    suite = ET.parse(path).getroot()
    for key in counts:
        counts[key] += int(suite.get(key, 0))
images = ['factory.png', 'iterator.png', 'adapter.png', 'iterator-uml.png', 'adapter-uml.png']
if counts['tests'] < 9 or any(counts[k] for k in ['failures','errors','skipped']):
    raise SystemExit('Entrega no validada: ' + str(counts))
for name in images:
    if not (out / name).is_file():
        raise SystemExit('Falta imagen real o UML: ' + name)
styles = getSampleStyleSheet()
styles['Code'].fontSize = 6
styles['Code'].leading = 8
doc = Document()
doc.styles['Normal'].font.size = Pt(10)
pdf = []
def heading(text, level=1):
    doc.add_heading(text, level)
    pdf.append(Paragraph(escape(text), styles['Heading'+str(level)]))
def paragraph(text):
    doc.add_paragraph(text)
    pdf.append(Paragraph(escape(text), styles['BodyText']))
def picture(name, explanation):
    path = out / name
    doc.add_picture(str(path), width=Inches(6.1))
    with PILImage.open(path) as im:
        width, height = im.size
    scale = min(470/width, 560/height)
    pdf.append(Image(str(path), width=width*scale, height=height*scale))
    paragraph(explanation)

heading('Patroiak Proiektua 26 - version simplificada')
paragraph('Factory Method, Iterator y Adapter. YerayOrdoki/proiektua2, rama patroiak-proiektua26.')
paragraph('Requisitos: patroiakProiektua26.pdf, paginas 1-5. La factoria se ha simplificado manteniendo su llamada desde la presentacion.')
paragraph('Verificacion automatica: '+str(counts)+'. Capturas Swing con fixtures deterministas, no datos personales ni pruebas SOAP extremo a extremo.')
for block in (out / 'README.md').read_text(encoding='utf-8').split(chr(10)*2):
    if block.startswith('## '):
        heading(block[3:], 2)
    elif not block.startswith('# '):
        paragraph(block.replace('```sh','').replace('```','').replace(chr(10),' '))
heading('UML ampliado')
for name, title in [('factory.png','Factory Method'),('iterator-uml.png','Iterator'),('adapter-uml.png','Adapter')]:
    heading(title, 2)
    picture(name, 'Diagrama UML de la version actual; fuente editable en el archivo .puml correspondiente.')
heading('Evidencias de ejecucion')
picture('iterator.png', 'Iterator: primero del ultimo al primero y despues del primero al ultimo, ejecutando PatternsDemo.describe.')
picture('adapter.png', 'Adapter: ventana SellerTable y JTable reales usando SellerAdapter; datos de demostracion no persistidos.')
heading('Codigo entregado y lineas importantes')
notes = {
 'BLFactory.java': 'Una sola clase y un if/else: el booleano selecciona createLocalBusinessLogic o createRemoteBusinessLogic. Esos dos metodos construyen la fachada y pueden sobrescribirse en pruebas. No hay clases anidadas, atributos de creadores ni constructores de inyeccion.',
 'BLFacade.java': 'getSalesIterator(desc) reutiliza getSales(desc). La sobrecarga sin argumentos usa busqueda vacia.',
 'RemoteBLFacade.java': 'Contrato SOAP original separado del estado local del iterador; conserva las 33 operaciones y el nombre XML BLFacade.',
 'BLFacadeImplementation.java': 'Cambio del endpointInterface a RemoteBLFacade y adicion de getSalesIterator(String); operaciones anteriores conservadas, con reformateo.',
 'RemoteFacade.java': 'Delegacion al puerto SOAP; el iterador heredado se crea en el cliente, no se serializa.',
 'ApplicationLauncher.java': 'La presentacion decide el modo con ConfigXML y obtiene la fachada mediante BLFactory. La ventana se crea en el EDT.',
 'ExtendedIterator.java': 'Contrato generico solicitado, ampliando Iterator con recorrido inverso y reposicionamiento.',
 'ListExtendedIterator.java': 'cursor es el elemento actual; previous devuelve antes de decrementar y goLast usa size-1. Se comprueban limites y remove no esta permitido.',
 'SellerAdapter.java': 'getValueAt traduce los campos de Sale a celdas. getColumnClass declara tipos sin depender de que exista una primera fila.',
 'SellerTable.java': 'JTable recibe SellerAdapter como TableModel. La sobrecarga permite pasar ventas obtenidas del servidor remoto.',
 'PatternsDemo.java': 'Demostracion inversa y directa; resuelve Seller por email en lugar de suponer que isLogged devuelve Seller.',
 'PatternsTest.java': 'Ocho pruebas unitarias. La prueba de la factoria sobrescribe solo los dos metodos protegidos de creacion para devolver mocks sin DB ni red.',
 'PatternsScreenshotTest.java': 'Ejecuta y captura ventanas Swing reales; la omision por entorno headless impide certificar la entrega.'
}
for path in sorted((root / 'src').rglob('*.java')):
    if path.name not in notes:
        continue
    heading(str(path.relative_to(root)), 2)
    paragraph(notes[path.name])
    source = path.read_text(encoding='utf-8')
    run = doc.add_paragraph().add_run(source)
    run.font.name = 'Consolas'; run.font.size = Pt(7)
    lines = []
    for number, line in enumerate(source.splitlines(), 1):
        lines.extend(textwrap.wrap(f'{number:3} {line}', width=112, replace_whitespace=False, drop_whitespace=False) or [''])
    for start in range(0,len(lines),45):
        pdf.append(Preformatted(chr(10).join(lines[start:start+45]),styles['Code']))
        pdf.append(Spacer(1,8))
heading('Limitaciones y entrega')
paragraph('No se certifican SOAP extremo a extremo ni ObjectDB real. Las cabeceras graficas del enunciado requieren contraste visual. El Word es adicional al patters.pdf exigido. No se ha subido ningun archivo a OneDrive.')
doc.save(out / 'patroiak.docx')
SimpleDocTemplate(str(root / 'patters.pdf'),leftMargin=40,rightMargin=40,topMargin=40,bottomMargin=40).build(pdf)
for path in [out/'patroiak.docx', root/'patters.pdf']:
    if not path.is_file() or path.stat().st_size == 0:
        raise SystemExit('Documento no generado: '+str(path))
(out/'validation.json').write_text(json.dumps(dict(validated=True,counts=counts,ci_run=os.environ.get('GITHUB_RUN_ID'),source_commit=os.environ.get('GITHUB_SHA')),indent=2),encoding='utf-8')
