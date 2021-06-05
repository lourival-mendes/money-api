-- Column: public.lancamento.urlAnexo

-- ALTER TABLE public.lancamento DROP COLUMN urlAnexo;

ALTER TABLE public.lancamento
    ADD COLUMN urlAnexo character varying(200) COLLATE pg_catalog."default";