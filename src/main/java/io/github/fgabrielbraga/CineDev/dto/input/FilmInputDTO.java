package io.github.fgabrielbraga.CineDev.dto.input;

import io.github.fgabrielbraga.CineDev.model.Film;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class FilmInputDTO {

    private UUID uuid;
    @NotBlank(message = "Por favor, forneça um título para o filme.")
    @Size(max = 120, message = "O número de caracteres do título excede o limite.")
    private String title;
    @NotBlank(message = "Por favor, forneça uma sinopse para o filme.")
    private String resume;
    @NotBlank(message = "Por favor, forneça uma lista de gêneros para o filme")
    @Size(max = 255, message = "O número de caracteres da lista de gêneros excede o limite.")
    private String genres;
    @NotNull(message = "Por favor, forneça uma duração para o filme.")
    @Min(value = 0, message = "O formato da duração é inválido. Certifique-se de que está correto.")
    private Float duration;
    @NotBlank(message = "Por favor, forneça uma imagem de capa para o filme.")
    private String coverImage;
    @NotNull(message = "Por favor, forneça uma data de lançamento para o filme.")
    private LocalDate publishedIn;

    public FilmInputDTO() {
    }

    public static Film parseFilm(FilmInputDTO filmInputDTO) {
        return Optional.ofNullable(filmInputDTO).map(dto -> {
            Film film = new Film();
            film.setUuid(dto.getUuid());
            film.setTitle(dto.getTitle());
            film.setResume(dto.getResume());
            film.setGenres(dto.getGenres());
            film.setDuration(dto.getDuration());
            film.setCoverImage(dto.getCoverImage());
            film.setPublishedIn(dto.getPublishedIn());
            return film;
        }).orElse(null);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDate getPublishedIn() {
        return publishedIn;
    }

    public void setPublishedIn(LocalDate publishedIn) {
        this.publishedIn = publishedIn;
    }
}
