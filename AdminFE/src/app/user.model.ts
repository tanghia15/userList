// user.model.ts
export interface User {
    id: number;
    fullname: string;
    phone_number: string;
    address: string;
    password?: string;
    created_at?: Date;
    updated_at?: Date;
    is_active?: boolean;
    date_of_birth: Date;
    facebook_account_id?: number;
    google_account_id?: number;
  }
  