package br.com.alura.screenmatch.model;

public enum Category {

    ACTION("Action", "Ação"),
    ADVENTURE("Adventure", "Aventura"),
    ANIMATION("Animation", "Animação"),
    BIOGRAPHY("Biography", "Biografia"),
    COMEDY("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DOCUMENTARY("Documentary", "Documentário"),
    DRAMA("Drama", "Drama"),
    FAMILY("Family", "Família"),
    FANTASY("Fantasy", "Fantasia"),
    FILM_NOIR("Film-Noir", "Film-Noir"),
    GAME_SHOW("Game-Show", "Game-Show"),
    HISTORY("History", "História"),
    HORROR("Horror", "Terror"),
    MUSIC("Music", "Música"),
    MUSICAL("Musical", "Musical"),
    MYSTERY("Mystery", "Mistério"),
    NEWS("News", "Notícias"),
    REALITY_TV("Reality-TV", "Reality-TV"),
    ROMANCE("Romance", "Romance"),
    SCIFI("Sci-Fi", "Ficção Científica"),
    SHORT("Short", "Curta"),
    SPORT("Sport", "Esporte"),
    TALK_SHOW("Talk-Show", "Talk-Show"),
    THRILLER("Thriller", "Suspense"),
    WAR("War", "Guerra"),
    WESTERN("Western", "Faroeste");

    private final String omdbCategory;

    private final String ptBrCategory;

    Category(String omdbCategory, String ptBrCategory) {
        this.omdbCategory = omdbCategory;
        this.ptBrCategory = ptBrCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Category fromPtBrString(String text) {
        for (Category category : Category.values()) {
            if (category.ptBrCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
