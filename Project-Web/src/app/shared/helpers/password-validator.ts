import { AbstractControl } from "@angular/forms";

export const passwordValidator = (control: AbstractControl): null | { nomatch: boolean } => {
  const password = control.get('password');
  const repeatPassword = control.get('repeatPassword');

  if (password == null || repeatPassword == null) {
    return null;
  }

  return (password.value == repeatPassword.value) ? null : { nomatch: true };
};
