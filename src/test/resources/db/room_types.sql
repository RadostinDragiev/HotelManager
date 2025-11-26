INSERT INTO room_types (uuid, name, base_price_per_night, capacity, description)
VALUES (UNHEX(REPLACE('fcf4ed3a-00f0-4187-841c-8960dc3b07f5', '-', '')), 'STANDARD_SINGLE_ROOM', '80', '1',
        'Standard single person room'),
       (UNHEX(REPLACE('e5a9aead-0b4e-4bbd-9fb1-92b424f55b6c', '-', '')), 'STANDARD_DOUBLE_ROOM', '100', '2',
        'Standard double person room'),
       (UNHEX(REPLACE('299ffc12-b295-4cf9-a10e-2b3788157bfe', '-', '')), 'DELUXE_SINGLE_ROOM', '140', '1',
        'Deluxe single person room'),
       (UNHEX(REPLACE('ec5abac1-2370-427d-b3f0-f1cfcf883663', '-', '')), 'DELUXE_DOUBLE_ROOM', '160', '2',
        'Deluxe double person room'),
       (UNHEX(REPLACE('2a402e99-fe33-42d7-9f6d-0b2fe06a38d2', '-', '')), 'DELUXE_APARTMENT', '240', '4',
        'Deluxe apartment for small family'),
       (UNHEX(REPLACE('94d8e468-e908-493a-8f7f-dc4e825dd862', '-', '')), 'DELUXE_DOUBLE_APARTMENT', '300', '6',
        'Deluxe apartment for big family');