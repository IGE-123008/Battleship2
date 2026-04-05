package battleship;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {

    /**
     * Gera um ficheiro PDF com o histórico de jogadas.
     * @param historico Lista de movimentos (alienMoves) do jogo.
     */
    public static void gerarRelatorio(List<IMove> historico) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Historico_Battleship.pdf"));
            document.open();

            // Configuração de Fontes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            document.add(new Paragraph("RELATÓRIO DE JOGO - BATALHA NAVAL\n", titleFont));
            document.add(new Paragraph("Gerado automaticamente pelo sistema\n", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(new Paragraph("------------------------------------------------------------------\n\n"));

            if (historico.isEmpty()) {
                document.add(new Paragraph("Nenhuma jogada foi registada."));
            } else {
                for (IMove m : historico) {
                    document.add(new Paragraph("JOGADA Nº " + m.getNumber(), headerFont));
                    document.add(new Paragraph("Posições visadas: " + m.getShots().toString()));

                    String resultado = m.processEnemyFire(false);
                    document.add(new Paragraph("Resultado: " + resultado));
                    document.add(new Paragraph(" ")); // Espaçamento
                }
            }

            System.out.println("\n[PDF] Relatório gerado com sucesso: Historico_Battleship.pdf");

        } catch (Exception e) {
            System.err.println("Erro ao criar PDF: " + e.getMessage());
        } finally {
            document.close();    
        }
    }
}