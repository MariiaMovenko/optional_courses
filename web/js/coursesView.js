class CoursesView {

    constructor(coursesContainer) {
         this._coursesContainer = coursesContainer;
    }

    clear() {
        this._coursesContainer.empty();
    }

    appendCourse(course) {
        this._coursesContainer.append(course);
    }

}