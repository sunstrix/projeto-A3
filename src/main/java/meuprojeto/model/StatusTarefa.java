package meuprojeto.model;

public enum StatusTarefa {
    A_FAZER("A Fazer"),
    FAZENDO("Fazendo"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
    
    private final String descricao;
    
    StatusTarefa(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}