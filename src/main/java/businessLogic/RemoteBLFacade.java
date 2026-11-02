package businessLogic;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.awt.Image;
import javax.jws.WebMethod;
import javax.jws.WebService;
import dataAccess.Emaitza;
import domain.*;
import exceptions.*;
/** Existing SOAP contract, kept separate from the client-side iterator. */
@WebService(name = "BLFacade", targetNamespace = "http://businessLogic/")
public interface RemoteBLFacade {
    @WebMethod public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
    @WebMethod public List<Sale> getSales(String desc);
    @WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);
    @WebMethod public void initializeBD();
    @WebMethod public Image downloadImage(String imageName);
    @WebMethod public List<Complaint> getUnmanagedComplaints();
    @WebMethod public void markComplaintAsManaged(Integer complaintId);
    @WebMethod public Emaitza isLogged(String log, String password);
    @WebMethod public Emaitza isAdminLogged(int ID, String pass);
    @WebMethod public Emaitza isAdminRegistered(int ID, String pass);
    @WebMethod public Emaitza isRegistered(String mail, String user, String pass);
    @WebMethod public String getEmail(String name, String password);
    @WebMethod public void buySale(Sale s, Seller current);
    @WebMethod public boolean updateProfile(String email, String newName, String newPassword);
    @WebMethod public Seller getSellerByEmail(String email);
    @WebMethod public void updateMoney(String email, float amount);
    @WebMethod public void saveComplaint(String message, Seller complainant, Sale sale);
    @WebMethod public void addProductToBasket(String email, int saleNumber);
    @WebMethod public void buyMultipleSales(List<Sale> sales, Seller buyer);
    @WebMethod public List<Sale> getBasket(String email);
    @WebMethod public void clearBasket(String email);
    @WebMethod public void createEskaera(String title, String status, String description, String sellerMail);
    @WebMethod public List<Eskaera> getEskaerak(String search);
    @WebMethod public Eskaera getEskaeraByID(Integer id);
    @WebMethod public boolean createEskaeraOferta(Integer eskID, String selEmail, float amount, String message);
    @WebMethod public List<Eskaera> getMyEskaerak(Seller s);
    @WebMethod public List<EskaeraOferta> getOffersByEskaera(Eskaera esk);
    @WebMethod public Sale createSaleFromEskaeraOfer(Integer id);
    @WebMethod public void createReview(String authorEmail, int saleNumber, String comment, int stars);
    @WebMethod public List<Sale> getBoughtsBySeller(String email);
    @WebMethod public List<Sale> getSalesBySeller(String email);
    @WebMethod public List<Review> getReviewsBySeller(String email);
    @WebMethod public String getSellerEmailBySale(Integer saleId);
}
