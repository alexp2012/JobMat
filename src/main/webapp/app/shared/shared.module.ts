import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { JobMatSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [JobMatSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [JobMatSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobMatSharedModule {
  static forRoot() {
    return {
      ngModule: JobMatSharedModule
    };
  }
}
