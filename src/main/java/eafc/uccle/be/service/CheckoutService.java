package eafc.uccle.be.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.element.Image;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.dao.ShoppingDetailsRepository;
import eafc.uccle.be.dao.ShoppingOrderRepository;
import eafc.uccle.be.dao.UserRepository;
import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.dto.ShoppingDetailsDto;
import eafc.uccle.be.entity.Product;
import eafc.uccle.be.entity.ShoppingDetails;
import eafc.uccle.be.entity.ShoppingOrder;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CheckoutService {

    private final ShoppingOrderRepository shoppingOrderRepository;
    private final ShoppingDetailsRepository shoppingDetailsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CheckoutService(ShoppingOrderRepository shoppingOrderRepository, ShoppingDetailsRepository shoppingDetailsRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.shoppingOrderRepository = shoppingOrderRepository;
        this.shoppingDetailsRepository = shoppingDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    public Map<String, Object> placeOrder(Purchase purchase) {

        ShoppingOrder order = new ShoppingOrder();
        order.setTotalPrice(purchase.getOrder().getTotalPrice());
        order.setTotalQuantity(purchase.getOrder().getTotalQuantity());
        order.setBillingAddress(purchase.getOrder().getBillingAddress());
        order.setShippingAddress(purchase.getOrder().getShippingAddress());
        order.setPaymentMethod(purchase.getOrder().getPaymentMethod());
        order.setPaymentStatus(purchase.getOrder().getPaymentStatus());
        order.setDeliveryMethod(purchase.getOrder().getDeliveryMethod());
        order.setDeliveryStatus(purchase.getOrder().getDeliveryStatus());
        order.setOrderDate(new Date());
        order.setUser(userRepository.findById(purchase.getUser().getId()).orElse(null));

        ShoppingOrder savedOrder = shoppingOrderRepository.save(order);

        for (ShoppingDetailsDto item : purchase.getOrderItems()) {
            Product p = productRepository.findById((long) item.getIdProduct()).orElse(null);
            ShoppingDetails newShoppingDetails = new ShoppingDetails();
            newShoppingDetails.setQuantity(item.getQuantity());
            newShoppingDetails.setUnitPrice(item.getUnitPrice());
            newShoppingDetails.setSubTotalPrice(item.getSubTotalPrice());
            newShoppingDetails.setShoppingOrder(savedOrder);
            newShoppingDetails.setProduct(p);
            shoppingDetailsRepository.save(newShoppingDetails);

            // Update units in stock
            p.setUnitsInStock(p.getUnitsInStock() - item.getQuantity());
            productRepository.save(p);

        }

        byte[] pdfBytes = generateOrderPdf(purchase, savedOrder.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("pdfBytes", pdfBytes);
        result.put("orderId", savedOrder.getId());

        return result;
    }


    public byte[] generateOrderPdf(Purchase purchase, String newId) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Chemin de l'image dans le dossier resources
            String imagePath = Paths.get("src/main/resources/images/logo.png").toAbsolutePath().toString();
            ImageData imageData = ImageDataFactory.create(imagePath);

            // Initialisation du document PDF
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            // Ajouter la première page explicitement
            PdfPage firstPage = pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);

            // Ajouter l'image de fond sur la première page
            PdfCanvas canvas = new PdfCanvas(firstPage);
            Rectangle pageSize = pdfDocument.getDefaultPageSize();
            PdfExtGState gs1 = new PdfExtGState();
            gs1.setFillOpacity(0.1f);
            canvas.saveState();
            canvas.setExtGState(gs1);
            canvas.addImage(imageData, pageSize.getWidth(), 0, 0, pageSize.getHeight(), 0, 0);
            canvas.restoreState();

            // Labels
            Map<String, String> labels = getLabels();

            // Paramètres
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_receipt", "ORD-" + newId);
            parameters.put("date", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            parameters.put("billingAddress", purchase.getOrder().getBillingAddress());
            parameters.put("shippingAddress", purchase.getOrder().getShippingAddress());
            parameters.put("firstname", purchase.getUser().getFirstname());
            parameters.put("lastname", purchase.getUser().getLastname());
            parameters.put("gender", purchase.getUser().getGender());
            parameters.put("id", String.valueOf(purchase.getUser().getId()));
            parameters.put("email", purchase.getUser().getEmail());
            parameters.put("phoneNumber", purchase.getUser().getPhoneNumber());
            parameters.put("totalQuantity", String.valueOf(purchase.getOrder().getTotalQuantity()));
            parameters.put("totalPrice", String.valueOf(purchase.getOrder().getTotalPrice()));
            parameters.put("deliveryPrice", purchase.getOrder().getDeliveryMethod().split("-")[1].trim());

            List<ShoppingDetailsDto> orderItems = purchase.getOrderItems().stream().toList();

            // Création des tableaux
            float threecol = 190f;
            float twocol = 285f;
            float twocol150 = twocol + 150f;
            float[] twoColumnWidth = {twocol150, twocol};
            float[] fiveColumnWidth = {threecol, threecol, threecol, threecol, threecol};
            float[] fullWidth = {threecol * 3};
            Paragraph onesp = new Paragraph("\n");

            Table table = new Table(twoColumnWidth);
            table.addCell(new Cell().add("NextGenMedia").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
            Table nestedTableLeft = new Table(new float[]{twocol / 2, twocol / 2});
            nestedTableLeft.addCell(getHeaderTextCell("Invoice Number:"));
            nestedTableLeft.addCell(getHeaderTextCellValue(parameters.get("id_receipt")));
            nestedTableLeft.addCell(getHeaderTextCell("Invoice Date:"));
            nestedTableLeft.addCell(getHeaderTextCellValue(parameters.get("date")));

            table.addCell(new Cell().add(nestedTableLeft).setBorder(Border.NO_BORDER));

            Border gb = new SolidBorder(Color.GRAY, 2f);
            Table divider = new Table(fullWidth);
            divider.setBorder(gb);
            document.add(table);
            document.add(onesp);
            document.add(divider);
            document.add(onesp);

            // Section pour les adresses et infos client
            Table twoColTable = new Table(twoColumnWidth);
            twoColTable.addCell(getBillingAndShippingCell(labels.get("BillingAddressLabel")));
            twoColTable.addCell(getBillingAndShippingCell(labels.get("ShippingAddressLabel")));
            document.add(twoColTable.setMarginBottom(12f));

            Table twoColTable2 = new Table(twoColumnWidth);
            twoColTable2.addCell(getCell10Left(parameters.get("billingAddress"), false));
            twoColTable2.addCell(getCell10Left(parameters.get("shippingAddress"), false));
            document.add(twoColTable2);

            float[] oneColumnWidth = {twocol150};
            Table oneColTable1 = new Table(oneColumnWidth);
            oneColTable1.addCell(getCell10Left("Customer Info", true));
            oneColTable1.addCell(getCell10Left(labels.get("idUserLabel") + parameters.get("id"), false));
            oneColTable1.addCell(getCell10Left(labels.get("firstNameLabel") + parameters.get("firstname"), false));
            oneColTable1.addCell(getCell10Left(labels.get("lastNameLabel") + parameters.get("lastname"), false));
            oneColTable1.addCell(getCell10Left(labels.get("genderLabel") + parameters.get("gender"), false));
            oneColTable1.addCell(getCell10Left(labels.get("emailLabel") + parameters.get("email"), false));
            oneColTable1.addCell(getCell10Left(labels.get("phoneNumberLabel") + parameters.get("phoneNumber"), false));
            document.add(oneColTable1.setMarginBottom(10f));

            Table tableDivider2 = new Table(fullWidth);
            DashedBorder dgb = new DashedBorder(Color.GRAY, 0.5f);
            document.add(tableDivider2.setBorder(dgb));

            // Section Produits
            Paragraph productParagraph = new Paragraph("Products");
            document.add(productParagraph.setBold());

            Table fiveColTable1 = new Table(fiveColumnWidth);
            fiveColTable1.setBackgroundColor(Color.BLACK, 0.7f);
            fiveColTable1.addCell(new Cell().add("Id Product").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
            fiveColTable1.addCell(new Cell().add("Name").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            fiveColTable1.addCell(new Cell().add("Unit Price").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            fiveColTable1.addCell(new Cell().add("Quantity").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
            fiveColTable1.addCell(new Cell().add("SubTotal").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));

            document.add(fiveColTable1);

            for (ShoppingDetailsDto item : orderItems) {
                Table itemRow = new Table(fiveColumnWidth);
                itemRow.addCell(new Cell().add(String.valueOf(item.getIdProduct())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                itemRow.addCell(new Cell().add(item.getName()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                itemRow.addCell(new Cell().add(item.getUnitPrice()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                itemRow.addCell(new Cell().add(String.valueOf(item.getQuantity())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                itemRow.addCell(new Cell().add(item.getSubTotalPrice()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
                document.add(itemRow);
            }


            // Ligne pour la quantité totale en bas de la colonne "Quantity"
            Table totalQuantityRow = new Table(fiveColumnWidth);
            totalQuantityRow.addCell(new Cell(1, 3).add("").setBorder(Border.NO_BORDER)); // Cells vides pour aligner
            totalQuantityRow.addCell(new Cell().add(labels.get("totalQuantityLabel")).setBorder(Border.NO_BORDER).setBold().setMarginRight(20f));
            totalQuantityRow.addCell(new Cell().add(parameters.get("totalQuantity")).setBorder(Border.NO_BORDER).setMarginRight(55f));
            document.add(totalQuantityRow);

            // Ligne pour le prix de la livraison
            Table deliveryPriceRow = new Table(fiveColumnWidth);
            deliveryPriceRow.addCell(new Cell(1, 3).add("").setBorder(Border.NO_BORDER)); // Cells vides pour aligner
            deliveryPriceRow.addCell(new Cell().add("Delivery Price").setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
            deliveryPriceRow.addCell(new Cell().add(parameters.get("deliveryPrice")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            document.add(deliveryPriceRow);

            // Ligne pour le total
            Table totalPriceRow = new Table(fiveColumnWidth);
            totalPriceRow.addCell(new Cell(1, 3).add("").setBorder(Border.NO_BORDER)); // Cells vides pour aligner
            totalPriceRow.addCell(new Cell().add(labels.get("totalPriceLabel")).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
            totalPriceRow.addCell(new Cell().add(parameters.get("totalPrice")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            document.add(totalPriceRow);

            document.add(onesp);
            document.add(divider);
            document.add(onesp);

            // Add "TERMS AND CONDITIONS" section
            Paragraph termsTitle = new Paragraph("TERMS AND CONDITIONS").setBold().setFontSize(12f);
            document.add(termsTitle);

            Paragraph termsText = new Paragraph("All purchases are subject to our standard terms and conditions. "
                    + "For more details, please visit our website or contact our customer service.");
            termsText.setFontSize(10f).setTextAlignment(TextAlignment.JUSTIFIED);
            document.add(termsText);

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }





    private static Map<String, String> getLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("BillingAddressLabel", "Billing Information");
        labels.put("ShippingAddressLabel", "Shipping Information");
        labels.put("firstNameLabel", "Firstname : ");
        labels.put("lastNameLabel", "Lastname : ");
        labels.put("genderLabel", "Gender : ");
        labels.put("idUserLabel", "Id Number : ");
        labels.put("emailLabel", "Email Address : ");
        labels.put("phoneNumberLabel", "Phone Number : ");
        labels.put("totalQuantityLabel", "Total Quantity");
        labels.put("totalPriceLabel", "Total Price");
        return labels;
    }

    public Cell getHeaderTextCell(String textValue) {
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    public Cell getHeaderTextCellValue(String textValue) {
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    public Cell getBillingAndShippingCell(String textValue) {
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    public Cell getCell10Left(String textValue, boolean isBold) {
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }

}
