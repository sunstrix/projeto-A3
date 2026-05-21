package meuprojeto.service;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        // Remove caracteres especiais
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (10 - i);
        }

        int firstDigit = 11 - (sum % 11);
        firstDigit = firstDigit >= 10 ? 0 : firstDigit;

        if (firstDigit != Integer.parseInt(String.valueOf(cpf.charAt(9)))) {
            return false;
        }

        // Calcula segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (11 - i);
        }

        int secondDigit = 11 - (sum % 11);
        secondDigit = secondDigit >= 10 ? 0 : secondDigit;

        return secondDigit == Integer.parseInt(String.valueOf(cpf.charAt(10)));
    }
}