package businessLogic;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import configuration.ConfigXML;
import dataAccess.Emaitza;
import domain.*;
import exceptions.*;
/** ConcreteProduct: SOAP delegate plus the inherited client-side iterator. */
public final class RemoteFacade implements BLFacade {
    private final RemoteBLFacade service;
    public RemoteFacade(RemoteBLFacade service) {
        this.service = Objects.requireNonNull(service, "service");
    }
    public static BLFacade connect() {
        ConfigXML c = ConfigXML.getInstance();
        String wsdl = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort()
            + "/ws/" + c.getBusinessLogicName() + "?wsdl";
        try {
            Service s = Service.create(new URL(wsdl),
                new QName("http://businessLogic/", "BLFacadeImplementationService"));
            return new RemoteFacade(s.getPort(RemoteBLFacade.class));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid business logic URL: " + wsdl, e);
        }
    }
    @Override public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException { return service.createSale(title, description, status, price, pubDate, sellerEmail, file); }
    @Override public List<Sale> getSales(String desc) { return service.getSales(desc); }
    @Override public List<Sale> getPublishedSales(String desc, Date pubDate) { return service.getPublishedSales(desc, pubDate); }
    @Override public void initializeBD() { service.initializeBD(); }
    @Override public Image downloadImage(String imageName) { return service.downloadImage(imageName); }
    @Override public List<Complaint> getUnmanagedComplaints() { return service.getUnmanagedComplaints(); }
    @Override public void markComplaintAsManaged(Integer complaintId) { service.markComplaintAsManaged(complaintId); }
    @Override public Emaitza isLogged(String log, String password) { return service.isLogged(log, password); }
    @Override public Emaitza isAdminLogged(int ID, String pass) { return service.isAdminLogged(ID, pass); }
    @Override public Emaitza isAdminRegistered(int ID, String pass) { return service.isAdminRegistered(ID, pass); }
    @Override public Emaitza isRegistered(String mail, String user, String pass) { return service.isRegistered(mail, user, pass); }
    @Override public String getEmail(String name, String password) { return service.getEmail(name, password); }
    @Override public void buySale(Sale s, Seller current) { service.buySale(s, current); }
    @Override public boolean updateProfile(String email, String newName, String newPassword) { return service.updateProfile(email, newName, newPassword); }
    @Override public Seller getSellerByEmail(String email) { return service.getSellerByEmail(email); }
    @Override public void updateMoney(String email, float amount) { service.updateMoney(email, amount); }
    @Override public void saveComplaint(String message, Seller complainant, Sale sale) { service.saveComplaint(message, complainant, sale); }
    @Override public void addProductToBasket(String email, int saleNumber) { service.addProductToBasket(email, saleNumber); }
    @Override public void buyMultipleSales(List<Sale> sales, Seller buyer) { service.buyMultipleSales(sales, buyer); }
    @Override public List<Sale> getBasket(String email) { return service.getBasket(email); }
    @Override public void clearBasket(String email) { service.clearBasket(email); }
    @Override public void createEskaera(String title, String status, String description, String sellerMail) { service.createEskaera(title, status, description, sellerMail); }
    @Override public List<Eskaera> getEskaerak(String search) { return service.getEskaerak(search); }
    @Override public Eskaera getEskaeraByID(Integer id) { return service.getEskaeraByID(id); }
    @Override public boolean createEskaeraOferta(Integer eskID, String selEmail, float amount, String message) { return service.createEskaeraOferta(eskID, selEmail, amount, message); }
    @Override public List<Eskaera> getMyEskaerak(Seller s) { return service.getMyEskaerak(s); }
    @Override public List<EskaeraOferta> getOffersByEskaera(Eskaera esk) { return service.getOffersByEskaera(esk); }
    @Override public Sale createSaleFromEskaeraOfer(Integer id) { return service.createSaleFromEskaeraOfer(id); }
    @Override public void createReview(String authorEmail, int saleNumber, String comment, int stars) { service.createReview(authorEmail, saleNumber, comment, stars); }
    @Override public List<Sale> getBoughtsBySeller(String email) { return service.getBoughtsBySeller(email); }
    @Override public List<Sale> getSalesBySeller(String email) { return service.getSalesBySeller(email); }
    @Override public List<Review> getReviewsBySeller(String email) { return service.getReviewsBySeller(email); }
    @Override public String getSellerEmailBySale(Integer saleId) { return service.getSellerEmailBySale(saleId); }
}
