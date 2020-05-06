class CardPicker {

    constructor(cardPickerElement) {
        this._cardPickerElement = cardPickerElement;
        this._initOpenPickerButton();
    }

    _initOpenPickerButton() {
        let openPickerButton = this._cardPickerElement.find("button");
        let pickerTitle = openPickerButton.data("pickertitle");
        let pickerIdentifier = openPickerButton.data("target");
        this._cardPickerElement.append(this._getPickerDialog(pickerIdentifier, pickerTitle));
        this._pickerDialog = this._cardPickerElement.find(pickerIdentifier);
        openPickerButton.on("click", () => this._toggleVisibility());
        let cards = this._pickerDialog.find(".picker-card");
        cards.each(function() {
            let card = $(this);
            card.on("click", () => card.toggleClass("active"));
        });
    }

    _getPickerDialog(pickerIdentifier, pickerTitle) {
        return `
            <div class="modal fade ${pickerIdentifier.substring(1, pickerIdentifier.length)}" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
             <div class="modal-dialog modal-lg">
               <div class="modal-content">
                 <div class="modal-header">
                     <h5 class="modal-title" id="exampleModalLabel">${pickerTitle}</h5>
                     <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                       <span aria-hidden="true">&times;</span>
                     </button>
                   </div>
                   <div class="modal-body picker-cards">
                    <div class="card picker-card">
                      <div class="card-body">
                        Some theme
                      </div>
                    </div>
                    <div class="card picker-card">
                      <div class="card-body">
                        Some theme
                      </div>
                    </div>
                    <div class="card picker-card">
                      <div class="card-body">
                        Some theme
                      </div>
                    </div>
                    <div class="card picker-card">
                      <div class="card-body">
                        Some theme
                      </div>
                    </div>
                   </div>
                   <div class="modal-footer">
                       <button type="button" class="btn btn-primary">Submit</button>
                     </div>
               </div>
             </div>
           </div>
        `;
    }

    _toggleVisibility() {
        let isHidden = this._pickerDialog.attr("aria-hidden");
        this._pickerDialog.attr("aria-hidden", !isHidden);
    }

}