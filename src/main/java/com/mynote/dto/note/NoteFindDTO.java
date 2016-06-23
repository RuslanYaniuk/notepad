package com.mynote.dto.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynote.dto.common.PageRequestDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
public class NoteFindDTO extends AbstractNoteDTO {

    @JsonIgnore
    private PageRequestDTO pageRequestDTO = new PageRequestDTO(0, 20);

    public String getId() {
        return note.getId();
    }

    public void setId(String id) {
        note.setId(id);
    }

    public String getSubject() {
        return note.getSubject();
    }

    public void setSubject(String subject) {
        note.setSubject(subject);
    }

    public String getText() {
        return note.getText();
    }

    public void setText(String text) {
        note.setText(text);
    }

    public void setPageNumber(int pageNumber) {
        pageRequestDTO.setPageNumber(pageNumber);
    }

    public int getPageNumber() {
        return pageRequestDTO.getPageNumber();
    }

    public void setPageSize(int pageSize) {
        pageRequestDTO.setPageSize(pageSize);
    }

    public int getPageSize() {
        return pageRequestDTO.getPageSize();
    }

    @JsonIgnore
    public Pageable getPage() {
        return pageRequestDTO;
    }

    @JsonIgnore
    public void setPage(Pageable page) {
        this.pageRequestDTO = (PageRequestDTO) page;
    }
}
