-- Column: public.lancamento.url_anexo

-- ALTER TABLE public.lancamento DROP COLUMN url_anexo;

ALTER TABLE public.lancamento
    ADD COLUMN url_anexo character varying(200) COLLATE pg_catalog."default";