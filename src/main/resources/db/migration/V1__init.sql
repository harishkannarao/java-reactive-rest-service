CREATE TABLE customer_table (
    data jsonb
);

CREATE UNIQUE INDEX unique_customer_id ON customer_table using btree ((cast(data->>'id' as UUID)));