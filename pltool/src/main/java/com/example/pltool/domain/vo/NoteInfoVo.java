package com.example.pltool.domain.vo;


import java.util.List;

import com.example.pltool.domain.entity.Note;
import com.example.pltool.domain.entity.Question;

public class NoteInfoVo {
  private List<Question> questionList;
  private Note note;

  public List<Question> getQuestionList() {
    return questionList;
  }

  public void setQuestionList(List<Question> questionList) {
    this.questionList = questionList;
  }

  public Note getNote() {
    return note;
  }

  public void setNote(Note note) {
    this.note = note;
  }
}
