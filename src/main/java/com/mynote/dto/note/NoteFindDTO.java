package com.mynote.dto.note;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteFindDTO {

    private String id;

    public NoteFindDTO() {
    }

    public NoteFindDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteFindDTO that = (NoteFindDTO) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NoteFindDTO{" +
                "id='" + id + '\'' +
                '}';
    }
}
