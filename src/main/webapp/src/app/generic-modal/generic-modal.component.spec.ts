import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericModalComponent } from './generic-modal.component';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe('GenericModalComponent', () => {
  let component: GenericModalComponent;
  let fixture: ComponentFixture<GenericModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
         GenericModalComponent
         ],
      providers: [
         NgbActiveModal
         ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenericModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the generic-modal', () => {
    expect(component).toBeTruthy();
  });
});
