# -*- coding: utf-8 -*-
from openerp import models, fields, api

class supplier_order(models.Model):
    _name = 'batoi_logic.supplier_order'
    quantity = fields.Integer(String='Quantity', required=True)
    request_date = fields.Date(String='Request Date', required=True)
    provider_id = fields.Many2one('batoi_logic.provider', 'Provider', required=True)
    product_id = fields.Many2one('batoi_logic.product', 'Product', required=True)
